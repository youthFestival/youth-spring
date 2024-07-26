package com.youth.server.dto;

import lombok.Data;

@Data
public class InquiryDTO {
    private String category;
    private String title;
    private String content;
    private boolean isSecret;
    private Integer festivalId;
    private Integer userUid;
    private String username;
}
