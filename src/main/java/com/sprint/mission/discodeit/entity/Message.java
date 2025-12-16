package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;


import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "messages")
public class Message extends BaseUpdateEntity {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", columnDefinition = "uuid", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", columnDefinition = "uuid")
    private User author;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MessageAttachment> attachments = new ArrayList<>();


    public Message(User author, Channel channel, String content) {
        this.author = author;
        this.content = content;
        this.channel = channel;

    }

    public void update(String newContent) {

        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;

        }


    }

    public void addAttachment(BinaryContent content) {
        MessageAttachment ma = new MessageAttachment(this, content);
        attachments.add(ma);
    }

    public void removeAttachment(BinaryContent content) {
        attachments.removeIf(ma -> ma.getAttachment().equals(content));
    }
}
