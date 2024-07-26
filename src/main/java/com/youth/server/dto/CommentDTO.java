package com.youth.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDTO {
    private int id;
    private String content;
    private String username;
    private LocalDateTime updatedAt;
}
