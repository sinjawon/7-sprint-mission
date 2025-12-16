package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor

@Getter
@Table(name = "Channels")
public class Channel extends BaseUpdateEntity {


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;


    public Channel(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public void update(String newName, String newDescription) {

        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;

        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;

        }

    }
}
