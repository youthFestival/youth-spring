package com.youth.server.controller;

import com.youth.server.dto.RestEntity;
import com.youth.server.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/booth")
public class BoothController {

    private final BoothService boothService;

    @GetMapping("/{boothId}/items")
    public RestEntity getBoothItems(@PathVariable(name="boothId") int boothId) {
        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("조회되었습니다")
                .put("items", boothService.findBoothItemById(boothId) )
                .build();
    }
}
