package fr.phenix333.awale.arena.service.runner;

import java.io.File;

public class JarBotRunner implements BotRunnerStrategy {

    @Override
    public Process run(File botFile, int playerNumber) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", botFile.getName(), String.valueOf(playerNumber));
        pb.directory(botFile.getParentFile());

        return pb.start();
    }

}
