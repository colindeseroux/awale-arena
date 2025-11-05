package fr.phenix333.awale.arena.service.runner;

import java.io.File;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Factory class to get the appropriate BotRunnerStrategy based on the bot file
 * type.
 * 
 * @author Colin de Seroux
 */
@Service
@RequiredArgsConstructor
public class BotRunnerFactory {

    private final JarBotRunner jarBotRunner;

    private final PythonBotRunner pythonBotRunner;

    private final ExeBotRunner exeBotRunner;

    /**
     * Gets the appropriate BotRunnerStrategy based on the bot file type.
     *
     * @param botFile -> File : the bot file
     * 
     * @return BotRunnerStrategy -> The bot runner strategy
     * 
     * @throws IllegalArgumentException -> If the bot language is unsupported
     */
    public BotRunnerStrategy getStrategy(File botFile) throws IllegalArgumentException {
        String name = botFile.getName().toLowerCase();

        switch (name.substring(name.lastIndexOf('.') + 1)) {
            case "jar":
                return this.jarBotRunner;
            case "py":
                return this.pythonBotRunner;
            case "exe":
                return this.exeBotRunner;
            default:
                return this.exeBotRunner;
        }
    }

}
