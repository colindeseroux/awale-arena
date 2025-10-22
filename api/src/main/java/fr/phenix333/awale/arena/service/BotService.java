package fr.phenix333.awale.arena.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.phenix333.awale.arena.model.Bot;
import fr.phenix333.awale.arena.model.Group;
import fr.phenix333.awale.arena.repository.BotRepository;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

/*
 * Service for bot-related operations.
 * 
 * @author Colin de Seroux
 */
@Service
@RequiredArgsConstructor
public class BotService {

    private static final MyLogger L = MyLogger.create(BotService.class);

    private final BotRepository botRepository;

    private final GroupService groupService;

    private final GameService gameService;

    /**
     * Save the bot file to the server.
     * 
     * @param file  -> MultipartFile : the bot file
     * @param botId -> Long : the bot ID
     * 
     * @throws IllegalStateException
     * @throws IOException
     */
    private void saveFile(MultipartFile file, Long botId) throws IllegalStateException, IOException {
        L.function("Save the bot file to the server | botId : {}", botId);

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        File newFile = new File(FileUtils.getUserDirectory(), "bots");
        newFile = new File(newFile, String.format("%d.%s", botId, extension));

        file.transferTo(newFile);
    }

    /**
     * Create a new bot.
     * 
     * @param bot  -> Bot : the bot to create
     * @param file -> MultipartFile : the bot file
     * 
     * @return Bot
     * 
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    public Bot createBot(Bot bot, MultipartFile file)
            throws IllegalArgumentException, IllegalStateException, IOException {
        L.function("Create a new bot | bot : {}", bot);

        Group group = this.groupService.createOrKeepGroup(bot.getGroup().getName());

        boolean alreadyExists = this.botRepository.alreadyExists(bot.getName(), group.getId());

        if (alreadyExists) {
            throw new IllegalArgumentException("BotNameAlreadyExists");
        }

        bot.setGroup(group);

        this.botRepository.save(bot);

        this.saveFile(file, bot.getId());

        gameService.addBotToTest(bot);

        return bot;
    }

    /**
     * Delete the bot file from the server.
     * 
     * @param botId -> Long : the bot ID
     */
    private void deleteFile(Long botId) {
        L.function("Delete the bot file from the server | botId : {}", botId);

        File botsDir = new File(FileUtils.getUserDirectory(), "bots");

        File[] files = botsDir.listFiles((dir, name) -> name.startsWith(botId.toString() + "."));

        if (files != null) {
            for (File file : files) {
                L.info("Deleting file: {}", file.getAbsolutePath());

                file.delete();
            }
        }
    }

    /**
     * Scheduled task to delete files of inactive bots daily at 1 AM.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    protected void deleteFilesOfInactivatedBots() {
        this.botRepository.findInactivatedBots().forEach(bot -> {
            L.info("Deleting inactivated bot: {}", bot);

            this.deleteFile(bot.getId());
        });
    }

    /**
     * Scheduled task to retest all activated bots daily at midnight.
     */
    @Scheduled(cron = "30 0 0 * * ?")
    protected void retestActivatedBots() {
        this.botRepository.findActivatedBots().forEach(bot -> {
            this.gameService.addBotToTest(bot);
        });
    }

}
