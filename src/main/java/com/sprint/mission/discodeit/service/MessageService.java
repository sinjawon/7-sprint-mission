package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(CreateMessageRequest request, List<BinaryContentCreateRequest> binaryContentCreateRequests);

    MessageDto find(UUID messageId);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable);

    MessageDto update(UUID messageId, UpdateMessageRequest request);

    void delete(UUID messageId);
}
