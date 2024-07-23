package com.youth.server.controller;

import com.youth.server.domain.Festival;
import com.youth.server.dto.RestEntity;
import com.youth.server.dto.festival.FestivalRequest;
import com.youth.server.service.FestivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Param 가져오는법
 *  @GetMapping("/api/greet/{name}")
 *     public String greet(@PathVariable(name = "name") String name) {
 *         return "Hello, " + name;
 *     }
 */
@RestController
@RequestMapping("/api/festival")
public class FestivalController {

    @Autowired
    FestivalService festivalService;

    /**
     * 일자에 맞는 축제 조회
     */
    @GetMapping("calender")
    public RestEntity findAllByYearAndMonth(@RequestParam int year, @RequestParam int month) {
        List<Festival> festivals = festivalService.findAllByYearAndMonth(year, month);
        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message(festivals.isEmpty()
                        ? "일자에 맞는 축제가 없습니다."
                        : "일자에 맞는 축제 %d개가 조회되었습니다.".formatted(festivals.size())
                )
                .put("festivals", festivals)
                .build();
    }


    //@TODO 페스티벌 검색 + 티켓 예매 정보 (메인페이지 +  마이페이지)
    @GetMapping
    public RestEntity findFestival(@RequestParam FestivalRequest data){
        List<Festival> festivals = festivalService.findFestival(data);
        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message(festivals.isEmpty()
                        ? "일자에 맞는 축제가 없습니다."
                        : "일자에 맞는 축제 %d개가 조회되었습니다.".formatted(festivals.size())
                )
                .put("festivals", festivals)
                .build();
    }
    /**
     Name	Type	Description	필수
     limit	INT	한페이지에서 보여줄 포스터의 갯수	X
     offset	INT 	보여줄 포스터 시작점	X ?
     sortBy	String	정렬 (”거리순”,”인기순”,”최신순”) order by

     거리순 : distance
     인기순 : interest
     최신순 : recent
     티켓예매순 :  reservation	X
     locality	String	지역명 	X
     categories	String	페스티벌
     대학축제
     전체	X
     search	String	목록 검색	X
     pick	String	찜 : choice (찜 목록)	X
     */


    //@TODO 페스티벌 상세 정보 조회

}
