package fr.phenix333.awale.arena;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import fr.phenix333.logger.MyLogger;

/*
 * Main class for the Awale Arena application.
 * 
 * @author Colin de Seroux
 */
@SpringBootApplication
@EnableScheduling
public class AwaleArena implements CommandLineRunner {

	private static final MyLogger L = MyLogger.create(AwaleArena.class);

	public static void main(String[] args) {
		SpringApplication.run(AwaleArena.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		L.function("Launching the main code");

		//
	}

}
