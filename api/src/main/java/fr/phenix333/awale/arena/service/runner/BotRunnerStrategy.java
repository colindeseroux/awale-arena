package fr.phenix333.awale.arena.service.runner;

import java.io.File;

public interface BotRunnerStrategy {

    Process run(File botFile, int playerNumber) throws Exception;

}
