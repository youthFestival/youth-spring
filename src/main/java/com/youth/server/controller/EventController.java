package com.youth.server.controller;

import com.youth.server.dto.RestEntity;
import com.youth.server.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;

    @GetMapping("{userId}")
    public RestEntity getEventsByUserId(@PathVariable(name = "userId") int userId) {

        // @TODO 이벤트 본인만 열람가능 기능 추가.

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .put("events", eventRepository.findAllByUserId(userId))
                .message("이벤트 조회 성공")
                .build();
    }
}
