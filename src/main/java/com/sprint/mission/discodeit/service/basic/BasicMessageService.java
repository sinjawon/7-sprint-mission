package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageDto;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final PageResponseMapper pageResponseMapper;
    private final MessageMapper messageMapper;
    private final BinaryContentService binaryContentService;


    @Override
    @Transactional
    public MessageDto create(CreateMessageRequest request, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        //둘의 uuid가 존재유무판단

        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널UUID가없어 :" + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("유저UUID가 없어 :" + request.authorId());
        }


        User author = userRepository.getReferenceById(request.authorId());
        Channel channel = channelRepository.getReferenceById(request.channelId());

        Message message = new Message(
                author,
                channel,
                request.content()
        );

        if (!binaryContentCreateRequests.isEmpty()) {
            List<BinaryContent> binaryContents = makeBinaryContentlist(binaryContentCreateRequests);

            for (BinaryContent bc : binaryContents) {
                message.addAttachment(bc);
            }
        }


        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageId) {
        return messageRepository
                .findById(messageId)
                .map(messageMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + messageId));

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt, Pageable pageable) {
        Slice<MessageDto> slice = messageRepository.findAllByChannelIdOrderByCreatedAtDesc(channelId,
                        Optional.ofNullable(createAt).orElse(Instant.now()),
                        pageable)
                .map(messageMapper::toDto);

        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
                    .createdAt();
        }

        return pageResponseMapper.fromSlice(slice, nextCursor);

    }

    @Override
    @Transactional
    public MessageDto update(UUID messageId, UpdateMessageRequest request) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("매시지아이디가 없어 " + messageId));

        message.update(request.newContent());

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {

        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("메시지 아이디 :" + messageId + "못찾았어요");
        }

        messageRepository.deleteById(messageId);
    }


    private List<BinaryContent> makeBinaryContentlist(List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        return binaryContentCreateRequests.stream()
                .map(binaryContentService::create)
                .toList();
    }


}

