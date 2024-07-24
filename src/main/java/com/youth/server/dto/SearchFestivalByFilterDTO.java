package com.youth.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SearchFestivalByFilterDTO {
    private int id;
    private String festivalName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String locationName;
    private String festivalThumbnail;
    private int viewCount;
    private LocalDateTime ticketOpen;
    private boolean isFavorite;
    private int favoriteUserCount;

    public SearchFestivalByFilterDTO(int id,String festivalName, LocalDateTime startDate, LocalDateTime endDate, String locationName, String festivalThumbnail, int viewCount, LocalDateTime ticketOpen, long favoriteUserCount) {
        this.id = id;
        this.festivalName = festivalName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationName = locationName;
        this.festivalThumbnail = festivalThumbnail;
        this.viewCount = viewCount;
        this.ticketOpen = ticketOpen;
        this.isFavorite = false;
        this.favoriteUserCount = (int) favoriteUserCount;
    }

}


