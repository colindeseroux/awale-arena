package fr.phenix333.awale.arena.service.runner;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bot runner strategy for Python bots.
 * 
 * @author Colin de Seroux
 */
@Component
public class PythonBotRunner extends AbstractSandboxedRunner {

    @Value("${python.path}")
    private String pythonPath;

    @Override
    public Process run(File botFile, int playerNumber) throws Exception {
        return this.buildProcess(botFile, List.of(pythonPath, botFile.getAbsolutePath(), String.valueOf(playerNumber)));
    }

}
