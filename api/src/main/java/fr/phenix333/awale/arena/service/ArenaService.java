package fr.phenix333.awale.arena.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import fr.phenix333.awale.arena.model.Game;
import fr.phenix333.awale.arena.service.runner.BotRunnerFactory;
import fr.phenix333.awale.arena.service.runner.BotRunnerStrategy;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the arena and bot games.
 * 
 * @author Colin de Seroux
 */
@Service
@RequiredArgsConstructor
public class ArenaService {

    private static final MyLogger L = MyLogger.create(ArenaService.class);

    /**
     * Get the ProcessBuilder for a bot file.
     * 
     * @param botFile      -> File : the bot file
     * @param playerNumber -> int : the player number
     * 
     * @return Process -> The process for the bot
     * 
     * @throws Exception
     */
    private Process getProcessBuilder(File botFile, int playerNumber) throws Exception {
        BotRunnerStrategy strategy = BotRunnerFactory.getStrategy(botFile);

        return strategy.run(botFile, playerNumber);
    }

    /**
     * Get the bot files for a game.
     * 
     * @param game -> Game : the game
     * 
     * @return File[] -> The bot files
     */
    private File[] getBotFiles(Game game) {
        L.function("Getting bot files for game | game : {}", game);

        File botsDir = new File(FileUtils.getUserDirectory(), "bots");

        File[] files = new File[2];

        files[0] = botsDir.listFiles((dir, name) -> name.startsWith(game.getBot1().getId().toString() + "."))[0];
        files[1] = botsDir.listFiles((dir, name) -> name.startsWith(game.getBot2().getId().toString() + "."))[0];

        return files;
    }

    /**
     * Wait for move.
     * 
     * @param reader        -> BufferedReader : the reader to read the move from
     * @param timeoutMillis -> long : the timeout in milliseconds
     * 
     * @return String -> The move or "TimeOut" if the timeout is reached
     */
    private String waitForMove(BufferedReader reader, long timeoutMillis) {
        L.function("Waiting for move | timeoutMillis : {}", timeoutMillis);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executor.submit(() -> {
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    L.debug("IOException in readLine: {}", e.getMessage());
                    return "TimeOut";
                }
            });

            try {
                String result = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                L.debug("Received move: {}", result);
                return result != null ? result : "TimeOut";
            } catch (TimeoutException e) {
                L.warn("Timeout reached after {} ms", timeoutMillis);
                future.cancel(true);
                return "TimeOut";
            } catch (Exception e) {
                L.error("Error while waiting for move", e);
                return "TimeOut";
            }
        } finally {
            executor.shutdownNow();

            try {
                if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    L.warn("Executor did not terminate gracefully, forcing shutdown");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                L.warn("Interrupted while waiting for executor termination");
                executor.shutdownNow();
            }
        }
    }

    /**
     * Run the game loop between two bots.
     * 
     * @param bot1 -> Bot : the first bot
     * @param bot2 -> Bot : the second bot
     * @param game -> Game : the game
     * 
     * @return Game -> The played game
     * 
     * @throws IOException
     */
    private Game runGameLoop(Process bot1, Process bot2, Game game) throws IOException {
        L.function("Running game loop between two bots | bot1 : {}, bot2 : {}, game : {}", bot1, bot2, game);

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(bot1.getInputStream()));
        BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(bot1.getOutputStream()));

        BufferedReader reader2 = new BufferedReader(new InputStreamReader(bot2.getInputStream()));
        BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(bot2.getOutputStream()));

        writer1.write("START");
        writer1.newLine();
        writer1.flush();

        while (!game.isFinished()) {
            if (game.getCurrentPlayer() == 0) {
                String move = this.waitForMove(reader1, 1000);

                game.addMove(move);

                if (move.equals("TimeOut")) {
                    L.warn("Timeout player 1");
                    game.setWinner(2);
                    break;
                }

                writer2.write(move);
                writer2.newLine();
                writer2.flush();
            } else {
                String move = this.waitForMove(reader2, 1000);

                game.addMove(move);

                if (move.equals("TimeOut")) {
                    L.warn("Timeout player 2");
                    game.setWinner(1);
                    break;
                }

                writer1.write(move);
                writer1.newLine();
                writer1.flush();
            }

            L.debug("Move played {} for player {}", game.getMoves().get(game.getMoves().size() - 1),
                    game.getCurrentPlayer());

            game.simulateLastMove();
        }

        writer1.write("END");
        writer1.newLine();
        writer1.flush();
        writer2.write("END");
        writer2.newLine();
        writer2.flush();

        return game;
    }

    /**
     * Playing a game between two bots.
     * 
     * @param game -> Game : the game to play
     * 
     * @return Game -> The played game
     */
    public Game playGame(Game game) {
        L.function("Playing a game between two bots | game : {}", game);

        File[] files = this.getBotFiles(game);

        System.out.println("Bot files: " + files[0].getAbsolutePath() + ", " + files[1].getAbsolutePath());

        Process bot1 = null;
        Process bot2 = null;

        try {
            bot2 = this.getProcessBuilder(files[1], 2);
            bot1 = this.getProcessBuilder(files[0], 1);

            game = this.runGameLoop(bot1, bot2, game);
        } catch (Exception e) {
            L.error("Error during game", e);
        } finally {
            // Force termination of processes
            if (bot1 != null) {
                try {
                    bot1.destroy();
                    if (!bot1.waitFor(1000, TimeUnit.MILLISECONDS)) {
                        L.warn("Bot1 process did not terminate gracefully, forcing kill");
                        bot1.destroyForcibly();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    L.warn("Interrupted while waiting for bot1 termination");
                    bot1.destroyForcibly();
                }
            }

            if (bot2 != null) {
                try {
                    bot2.destroy();
                    if (!bot2.waitFor(1000, TimeUnit.MILLISECONDS)) {
                        L.warn("Bot2 process did not terminate gracefully, forcing kill");
                        bot2.destroyForcibly();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    L.warn("Interrupted while waiting for bot2 termination");
                    bot2.destroyForcibly();
                }
            }
        }

        return game;
    }

}
