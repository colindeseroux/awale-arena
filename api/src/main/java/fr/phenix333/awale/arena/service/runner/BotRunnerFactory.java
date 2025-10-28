package fr.phenix333.awale.arena.service.runner;

import java.io.File;

public class BotRunnerFactory {

    public static BotRunnerStrategy getStrategy(File botFile) {
        String name = botFile.getName().toLowerCase();

        switch (name.substring(name.lastIndexOf('.') + 1)) {
            case "jar":
                return new JarBotRunner();
            case "py":
                return new PythonBotRunner();
            default:
                throw new IllegalArgumentException("Unsupported bot language: " + name);
        }
    }

}
