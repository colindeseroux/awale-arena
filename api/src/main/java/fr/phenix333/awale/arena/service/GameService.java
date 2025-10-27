package fr.phenix333.awale.arena.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import fr.phenix333.awale.arena.model.Bot;
import fr.phenix333.awale.arena.model.Game;
import fr.phenix333.awale.arena.model.Waiting;
import fr.phenix333.awale.arena.repository.BotRepository;
import fr.phenix333.awale.arena.repository.GameRepository;
import fr.phenix333.awale.arena.repository.WaitingRepository;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

/*
 * Service for game-related operations.
 * 
 * @author Colin de Seroux
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private static final MyLogger L = MyLogger.create(GameService.class);

    private final GameRepository gameRepository;

    private final BotRepository botRepository;

    private final WaitingRepository waitingRepository;

    private final ArenaService arenaService;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final Queue<Waiting> nextBotsToTest = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean inProgress = new AtomicBoolean(false);

    /**
     * Adding bot to test queue.
     * 
     * @param bot -> Bot : the bot to add to the test queue.
     */
    public void addBotToTest(Bot bot) {
        L.function("Adding bot to test queue | bot : {}", bot);
        L.info("Adding bot to test queue | bot : {}", bot.getId());

        Waiting waiting = new Waiting(bot);
        this.waitingRepository.save(waiting);

        this.nextBotsToTest.add(waiting);

        if (this.inProgress.compareAndSet(false, true)) {
            this.executor.submit(this::processQueue);
        }
    }

    /**
     * Process the bot queue iteratively to avoid stack overflow.
     */
    public void processQueue() {
        Waiting waiting;

        while ((waiting = this.nextBotsToTest.poll()) != null) {
            this.waitingRepository.delete(waiting);
            this.testBot(waiting.getBot());
        }

        this.inProgress.set(false);
    }

    /**
     * Test a bot.
     * 
     * @param bot -> Bot : the bot to test.
     */
    private void testBot(Bot bot) {
        L.function("Test a bot | bot : {}", bot);

        this.botRepository.findActivatedBotsAndNoMatchingBetweenForToday(bot).forEach(activatedBot -> {
            L.info("Playing game for bot testing | bot : {} | activatedBot : {}", bot.getId(), activatedBot.getId());

            if (bot.getId().equals(activatedBot.getId())) {
                L.info("Skipping game as both bots are the same | bot : {}", bot.getId());
                return;
            }

            this.duelBetween(bot, activatedBot);
            this.duelBetween(activatedBot, bot);
        });
    }

    /**
     * Duel between two bots.
     * 
     * @param bot1 -> Bot : first bot
     * @param bot2 -> Bot : second bot
     */
    private void duelBetween(Bot bot1, Bot bot2) {
        L.function("Duel between two bots | bot1 : {} | bot2 : {}", bot1, bot2);

        Game game = new Game(bot1, bot2);

        game = this.arenaService.playGame(game);

        L.info("Winner of the game between {} and {} is bot {}", bot1.getId(), bot2.getId(),
                game.getWinner());

        this.gameRepository.save(game);
    }

}
