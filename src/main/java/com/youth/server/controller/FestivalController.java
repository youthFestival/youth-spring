package com.youth.server.controller;

import com.youth.server.domain.Festival;
import com.youth.server.domain.Image;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Param 가져오는법
 *  @GetMapping("/api/greet/{name}")
 *     public String greet(@PathVariable(name = "name") String name) {
 *         return "Hello, " + name;
 *     }
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/festival")
public class FestivalController {

    private final FestivalService festivalService;

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
//    @GetMapping
//    public RestEntity findFestival(@RequestParam FestivalRequest data){
//        List<Festival> festivals = festivalService.findFestival(data);
//        return RestEntity.builder()
//                .status(HttpStatus.CREATED)
//                .message(festivals.isEmpty()
//                        ? "일자에 맞는 축제가 없습니다."
//                        : "일자에 맞는 축제 %d개가 조회되었습니다.".formatted(festivals.size())
//                )
//                .put("festivals", festivals)
//                .build();
//    }

    @GetMapping("{festivalId}")
    public RestEntity getFestivalDetail(@PathVariable(name="festivalId") int festivalId){
        Festival festival = festivalService.findFestivalById(festivalId);

        Optional<String> festivalThumbnail = festival.getImages().stream()
                .filter(image -> image.getCategory().equals(Image.Category.포스터))
                .map(Image::getImgUrl)
                .findFirst();

        String festivalThumbnailUrl = festivalThumbnail.orElse("이미지가 없습니다.");

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("festivalThumbnail", festivalThumbnailUrl)
                .put("festivalName", festival.getName())
                .put("description", festival.getDescription())
                .put("startDate", festival.getStartDate())
                .put("endDate", festival.getEndDate())
                .put("showTime", Duration.between(festival.getStageOpen(),festival.getStageClose()).toHours())
                .put("category", festival.getCategory())
                .put("organizer", festival.getOrganizer())
                .put("minAge", festival.getMinAge())
                .put("tel",festival.getTel())
                .put("organizerUrl", festival.getOrganizerUrl())
                .build();
    }

    // 라인업
    @GetMapping("{festivalId}/line-up")
    public RestEntity getFestivalLineUp(@PathVariable(name="festivalId") int festivalId){

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("lineUp", festivalService.getFestivalLineUpById(festivalId))
                .build();
    }

    // 축제 포스터
    @GetMapping("{festivalId}/poster")
    public RestEntity getFestivalPoster(@PathVariable(name="festivalId") int festivalId){
        Festival festival = festivalService.findFestivalById(festivalId);

        Optional<String> festivalPoster = festival.getImages().stream()
                .filter(image -> image.getCategory().equals(Image.Category.포스터))
                .map(Image::getImgUrl)
                .findFirst();

        String festivalPosterUrl = festivalPoster.orElse("이미지가 없습니다.");

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("posterImage", festivalPosterUrl)
                .build();
    }

    // 행사사진 포스터
    @GetMapping("{festivalId}/pictures")
    public RestEntity getFestivalPictures(@PathVariable(name="festivalId") int festivalId){
        Festival festival = festivalService.findFestivalById(festivalId);

        ArrayList<String> pictures = new ArrayList<>();

        festival.getImages().stream()
                .filter(image -> image.getCategory().equals(Image.Category.행사_사진))
                .forEach(image -> pictures.add(image.getImgUrl()));

        if(pictures.isEmpty()){
            throw new NotFoundException("행사에 이미지가 존재하지 않습니다.");
        }

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("posterImage", pictures)
                .build();
    }

//    /@TODO 주변 축제 추천

    // 부스
}
