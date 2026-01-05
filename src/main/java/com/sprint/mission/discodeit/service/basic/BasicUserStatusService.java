package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;


    @Override
    @Transactional
    public UserStatusDto create(UserStatusCreateRequest request) {

        UUID userId = request.userId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        Optional.ofNullable(user.getStatus())
                .ifPresent(status -> {
                    throw DuplicateUserStatusException.withUserId(userId);
                });

        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(user, lastActiveAt);

        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatusDto find(UUID userStatusId) {

        return userStatusRepository.findById(userStatusId)
                .map(userStatusMapper::toDto)
                .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {

        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));

        userStatus.update(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus byUserId = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.withUserId(userId));

        byUserId.update(newLastActiveAt);

        return userStatusMapper.toDto(byUserId);
    }

    @Override
    public void delete(UUID userStatusId) {

        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.withId(userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
