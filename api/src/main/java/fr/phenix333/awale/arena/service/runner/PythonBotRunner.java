package fr.phenix333.awale.arena.service.runner;

import java.io.File;

public class PythonBotRunner implements BotRunnerStrategy {

    @Override
    public Process run(File botFile, int playerNumber) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("python", botFile.getName(), String.valueOf(playerNumber));
        pb.directory(botFile.getParentFile());

        return pb.start();
    }

}
