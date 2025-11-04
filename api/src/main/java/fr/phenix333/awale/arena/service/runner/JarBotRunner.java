package fr.phenix333.awale.arena.service.runner;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bot runner strategy for Java JAR bots.
 * 
 * @author Colin de Seroux
 */
@Component
public class JarBotRunner extends AbstractSandboxedRunner {

    @Value("${java.path}")
    private String javaPath;

    @Override
    public Process run(File botFile, int playerNumber) throws Exception {
        return this.buildProcess(botFile,
                List.of(javaPath, "-jar", botFile.getAbsolutePath(), String.valueOf(playerNumber)));
    }

}
