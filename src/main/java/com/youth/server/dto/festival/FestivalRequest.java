package com.youth.server.dto.festival;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FestivalRequest {
    private int limit;
    private int offset;

    private String sortBy;
    private String locality;
    private String categories;
    private String search;
    private boolean isFavorite;
}
