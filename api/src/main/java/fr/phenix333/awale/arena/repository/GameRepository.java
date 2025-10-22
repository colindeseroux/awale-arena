package fr.phenix333.awale.arena.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.phenix333.awale.arena.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
