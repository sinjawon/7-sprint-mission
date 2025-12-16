package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@NoArgsConstructor

@Getter
@Table(name = "message_attachments")
public class MessageAttachment extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id")
    private Message message;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attachment_id")
    private BinaryContent attachment;


    public MessageAttachment(Message message, BinaryContent attachment) {
        this.message = message;
        this.attachment = attachment;
    }


}
