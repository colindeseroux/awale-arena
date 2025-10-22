package fr.phenix333.awale.arena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.phenix333.awale.arena.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g WHERE g.name = :name")
    Group findGroupByName(String name);

}
