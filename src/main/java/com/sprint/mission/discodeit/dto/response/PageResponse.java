package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> content,
        Object nextCursor,
        int number,
        int size,
        boolean hasNext,
        Long totalElements
) {
}
