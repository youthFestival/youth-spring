package com.youth.server.dto.festival;

import com.youth.server.domain.Festival;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FestivalRequest {
    private Integer limit;
    private Integer offset;

    private String sortBy;
    private String locality;
    private String categories;
    private String search;
    private Boolean isFavorite;
    private Boolean isOngoing;

    public Festival.Category getCategory(){
        if(categories == null) return null;
        if(categories.equals("대학축제")) return Festival.Category.대학축제;
        if(categories.equals("페스티벌")) return Festival.Category.페스티벌;

        return null;
    }

}
