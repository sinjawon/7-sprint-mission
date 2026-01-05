package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusControllerDocs {

    private final ReadStatusService readStatusService;


    @PostMapping
    public ResponseEntity<ReadStatusDto> createStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto readStatus = readStatusService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readStatus);
    }


    @PatchMapping(path = "{readStatusId}")
    public ResponseEntity<ReadStatusDto> updateStatus(@PathVariable UUID readStatusId,
                                                      @RequestBody ReadStatusUpdateReuqest request) {
        ReadStatusDto update = readStatusService.update(readStatusId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(update);
    }


    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findStatusByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatusDto> allByUserId = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allByUserId);
    }
}