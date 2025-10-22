package fr.phenix333.awale.arena.service;

import org.springframework.stereotype.Service;

import fr.phenix333.awale.arena.model.Group;
import fr.phenix333.awale.arena.repository.GroupRepository;
import fr.phenix333.logger.MyLogger;
import lombok.RequiredArgsConstructor;

/*
 * Service for group-related operations.
 * 
 * @author Colin de Seroux
 */
@Service
@RequiredArgsConstructor
public class GroupService {

    private static final MyLogger L = MyLogger.create(GroupService.class);

    private final GroupRepository groupRepository;

    /**
     * Create or keep a group.
     * 
     * @param name -> String : name of the group
     * 
     * @return Group
     */
    public Group createOrKeepGroup(String name) {
        L.function("Create or keep a group | name : {}", name);

        Group existingGroup = this.groupRepository.findGroupByName(name);

        if (existingGroup != null) {
            return existingGroup;
        }

        Group group = new Group(name);

        return this.groupRepository.save(group);
    }

}
