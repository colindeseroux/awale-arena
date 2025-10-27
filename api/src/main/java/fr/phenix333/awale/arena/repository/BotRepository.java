package fr.phenix333.awale.arena.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.phenix333.awale.arena.model.Bot;

public interface BotRepository extends JpaRepository<Bot, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Bot b WHERE b.name = :name AND b.group.id = :groupId")
    boolean alreadyExists(String name, Long groupId);

    @Query("SELECT b FROM Bot b WHERE b.activated = false")
    List<Bot> findInactivatedBots();

    @Query("SELECT b FROM Bot b WHERE b.activated = true")
    List<Bot> findActivatedBots();

    @Query("SELECT b FROM Bot b WHERE b.activated = true AND NOT EXISTS (SELECT 1 FROM Game g WHERE (g.bot1 = b OR g.bot2 = b) AND (g.bot1 = :bot OR g.bot2 = :bot) AND DATE(g.createdAt) = DATE(CURRENT_TIMESTAMP))")
    List<Bot> findActivatedBotsAndNoMatchingBetweenForToday(Bot bot);

}
