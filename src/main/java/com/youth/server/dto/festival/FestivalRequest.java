package com.youth.server.dto.festival;

import com.youth.server.domain.Festival;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FestivalRequest {
    private Integer limit;
    private Integer offset;

    private String sortBy;
    private String locality;
    private String category;
    private String search;
    private Boolean isFavorite;
    private Boolean isOngoing;

    public Festival.Category getCategory(){
        if(category == null) return null;
        if(category.equals("대학축제")) return Festival.Category.대학축제;
        if(category.equals("페스티벌")) return Festival.Category.페스티벌;

        return null;
    }

}
