package fr.phenix333.awale.arena.service.runner;

import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for bot runners that need to run in a sandboxed environment.
 * 
 * @author Colin de Seroux
 */
public abstract class AbstractSandboxedRunner implements BotRunnerStrategy {

    @Value("${sandbox.command}")
    private String sandboxCommand;

    /**
     * Builds a Process to run the bot in a sandboxed environment.
     *
     * @param botFile         -> File : the bot file
     * @param languageCommand -> List<String> : the command to run the bot in its
     *                        language
     * 
     * @return Process -> The process running the bot
     * 
     * @throws IOException -> If an error occurs while starting the bot
     */
    protected Process buildProcess(File botFile, List<String> languageCommand) throws IOException {
        List<String> command = new ArrayList<>();

        if (!sandboxCommand.isBlank()) {
            command.addAll(Arrays.asList(sandboxCommand.split(",")));
        }

        command.addAll(languageCommand);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(botFile.getParentFile());

        return pb.start();
    }

}
