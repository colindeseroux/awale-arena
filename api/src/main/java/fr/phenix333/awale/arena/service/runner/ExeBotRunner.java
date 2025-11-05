package fr.phenix333.awale.arena.service.runner;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Bot runner strategy for Executable bots.
 * 
 * @author Colin de Seroux
 */
@Component
public class ExeBotRunner extends AbstractSandboxedRunner {

    @Override
    public Process run(File botFile, int playerNumber) throws Exception {
        return this.buildProcess(botFile, List.of(botFile.getAbsolutePath(), String.valueOf(playerNumber)));
    }

}
