package fr.phenix333.awale.arena.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.phenix333.awale.arena.model.Bot;
import fr.phenix333.awale.arena.service.BotService;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * Controller for bot endpoints.
 * 
 * @author Colin de Seroux
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bot")
public class BotController {

    private static final MyLogger L = MyLogger.create(BotController.class);

    private final BotService botService;

    /**
     * Endpoint to add a new bot.
     * 
     * @param bot  -> Bot : the bot to add
     * @param file -> MultipartFile : the bot file
     * 
     * @return ResponseEntity<Bot> -> The created bot
     */
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Bot> addBot(@RequestPart("bot") Bot bot, @RequestPart("file") MultipartFile file)
            throws Exception {
        L.function("Add a new bot | bot : {} | file : {}", bot, file.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED).body(this.botService.createBot(bot, file));
    }

}
