package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@MappedSuperclass
public abstract class BaseUpdateEntity extends BaseEntity {

    @LastModifiedDate
    @Column(columnDefinition = "timestamp with time zone")
    private Instant updatedAt;


}
