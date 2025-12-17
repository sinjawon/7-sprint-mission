package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.content.BinaryContent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryRepository extends JpaRepository<BinaryContent, UUID> {


    List<BinaryContent> findAllByIdIn(List<UUID> ids);


    void deleteById(UUID contentId);


}
