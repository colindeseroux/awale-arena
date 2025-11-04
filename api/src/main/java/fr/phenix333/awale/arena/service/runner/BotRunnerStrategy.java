package fr.phenix333.awale.arena.service.runner;

import java.io.File;

/**
 * Interface for bot runner strategies.
 * 
 * @author Colin de Seroux
 */
public interface BotRunnerStrategy {

    /**
     * Runs the bot with the given file and player number.
     *
     * @param botFile      -> File : the bot file to run
     * @param playerNumber -> int : the player number (1, 2)
     * 
     * @return Process -> The process running the bot
     * 
     * @throws Exception -> If an error occurs while starting the bot
     */
    Process run(File botFile, int playerNumber) throws Exception;

}
