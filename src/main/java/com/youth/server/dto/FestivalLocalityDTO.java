package com.youth.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FestivalLocalityDTO {
    private String locality;
    private Long viewCount;
    private Long likes;
}
