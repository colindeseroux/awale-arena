package fr.phenix333.awale.arena.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import fr.phenix333.awale.arena.model.Game;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArenaService {

    private static final MyLogger L = MyLogger.create(ArenaService.class);

    private static final String[] languagesSupported = new String[] { "py" };
    private static final String[] cmds = new String[] { "python" };

    private ProcessBuilder getProcessBuilder(File botFile, int playerNumber) {
        String fileName = botFile.getName();

        for (int i = 0; i < languagesSupported.length; i++) {
            if (fileName.endsWith(languagesSupported[i])) {
                String[] cmdParts = cmds[i].split(" ");
                String[] firejailCmd = {}; // { "firejail", "--net=none", "--private=/bots", "--rlimit-as=500M" };
                String[] fullCommand = new String[firejailCmd.length + cmdParts.length + 2];

                System.arraycopy(firejailCmd, 0, fullCommand, 0, firejailCmd.length);
                System.arraycopy(cmdParts, 0, fullCommand, firejailCmd.length, cmdParts.length);
                fullCommand[firejailCmd.length + cmdParts.length] = botFile.getAbsolutePath();
                fullCommand[firejailCmd.length + cmdParts.length + 1] = String.valueOf(playerNumber);

                return new ProcessBuilder(fullCommand);
            }
        }

        throw new IllegalArgumentException("Unsupported bot file: " + fileName);
    }

    /**
     * Playing a game between two bots.
     * 
     * @param game -> Game : the game to play
     * 
     * @return Game : the played game
     */
    public Game playGame(Game game) {
        L.function("Playing a game between two bots | game : {}", game);

        File botsDir = new File(FileUtils.getUserDirectory(), "bots");

        File[] files = botsDir.listFiles((dir, name) -> name.startsWith(game.getBot1().getId().toString() + ".")
                || name.startsWith(game.getBot2().getId().toString() + "."));

        Process bot1 = null;
        Process bot2 = null;

        try {
            bot2 = getProcessBuilder(files[1], 2).start();
            Thread.sleep(3000);
            bot1 = getProcessBuilder(files[0], 1).start();

            BufferedReader reader1 = new BufferedReader(new InputStreamReader(bot1.getInputStream()));
            BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(bot1.getOutputStream()));

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(bot2.getInputStream()));
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(bot2.getOutputStream()));

            boolean finished = false;
            int currentPlayer = 1;

            while (!finished) {
                if (currentPlayer == 1) {
                    String move = reader1.readLine();

                    game.getMoves().add(move);

                    writer2.write(move);
                    writer2.newLine();
                    writer2.flush();

                    currentPlayer = 2;
                } else {
                    String move = reader2.readLine();

                    game.getMoves().add(move);

                    writer1.write(move);
                    writer1.newLine();
                    writer1.flush();

                    currentPlayer = 1;
                }

                L.debug("Move played {} for player {}", game.getMoves().get(game.getMoves().size() - 1), currentPlayer);

                // TODO
                // test move validity and game end condition
                finished = game.getMoves().size() >= 10;
            }

            writer1.write("END");
            writer1.newLine();
            writer1.flush();
            writer2.write("END");
            writer2.newLine();
            writer2.flush();
        } catch (IOException | InterruptedException e) {
            L.error("Error during game", e);
        } finally {
            if (bot1 != null) {
                bot1.destroy();
            }

            if (bot2 != null) {
                bot2.destroy();
            }
        }

        return game;
    }

}
