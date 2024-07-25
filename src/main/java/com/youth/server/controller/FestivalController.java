package com.youth.server.controller;

import com.youth.server.domain.Festival;
import com.youth.server.domain.Image;
import com.youth.server.dto.RestEntity;
import com.youth.server.dto.SearchFestivalByFilterDTO;
import com.youth.server.dto.festival.FestivalRequest;
import com.youth.server.exception.NotFoundException;
import com.youth.server.service.FestivalService;
import com.youth.server.service.UserService;
import com.youth.server.util.Const;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final JwtUtil jwtUtil;
    private final UserService userService;

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

    @GetMapping
    public RestEntity findFestival(@RequestParam(name= "limit" ,required = false, defaultValue = ""+Const.DEAFULT_LIMIT) Integer limit,
                                   @RequestParam(name= "offset", required = false, defaultValue = ""+Const.DEAFULT_OFFSET) Integer offset,
                                   @RequestParam(name= "sortBy", required = false) String sortBy,
                                   @RequestParam(name= "locality", required = false) String locality,
                                   @RequestParam(name= "categories", required = false) String categories,
                                   @RequestParam(name= "search", required = false) String search,
                                   @RequestParam(name= "isFavorite", required = false, defaultValue = "false") Boolean isFavorite,
                                   @RequestParam(name= "isOngoing", required = false) Boolean isOngoing,
                                   HttpServletRequest request
    ){
        FestivalRequest data = FestivalRequest.builder().limit(limit).offset(offset).sortBy(sortBy).locality(locality).categories(categories).search(search).isFavorite(isFavorite).build();


        // 로그인 여부 확인 (isFavorite)
        Optional<Integer> currentUid = jwtUtil.getUid(request);
        List<Festival> favoritesFestival = currentUid.map(festivalService::findFavoriteFestivalByUid).orElse(new ArrayList<>());

        List<Integer> favoriteFestivalIds = new ArrayList<>();

        for(Festival festival : favoritesFestival) {
            favoriteFestivalIds.add(festival.getId());
        }

        // 검색 조건에 맞는 축제 조회
        PageRequest pageRequest = PageRequest.of(
                data.getOffset() == 0 ? 0 :
                        data.getOffset() / data.getLimit()
        , data.getLimit());

        List<SearchFestivalByFilterDTO> filteredFestivals = festivalService.findFestival(data,pageRequest);
        Comparator<SearchFestivalByFilterDTO> comparator = null;

    // 정렬
        if (sortBy != null) {
            filteredFestivals.sort(
                    switch (sortBy) {
                        case "interest" ->
                                Comparator.comparing(SearchFestivalByFilterDTO::getFavoriteUserCount).reversed();
                        case "recent" -> Comparator.comparing(SearchFestivalByFilterDTO::getStartDate);
                        case "distance" -> Comparator.comparing(SearchFestivalByFilterDTO::getLocationName);
                        case "reservation" -> comparator = Comparator.comparing(SearchFestivalByFilterDTO::getTicketOpen, Comparator.nullsLast(Comparator.naturalOrder()));
                        default -> Comparator.comparing(SearchFestivalByFilterDTO::getId); // 기본 정렬 기준
                    });

            if (comparator != null) {
                filteredFestivals.stream()
                        .filter(f -> f.getTicketOpen() != null) // 널 값 필터링
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }

        }



        filteredFestivals.forEach(festival -> {
                        // 좋아하는 축제인지 확인
                        if(favoriteFestivalIds.contains(festival.getId())){
                            festival.setFavorite(true);
                        }});

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("festivals",filteredFestivals)
                .build();
    }

    // 축제 상세정보

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
                .put("geoLocation", festival.getGeoLocationId())
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


    // 주변 축제 추천
    @GetMapping("{festivalId}/recommendations")
    public RestEntity getRecommendFestival(@PathVariable(name="festivalId") int festivalId,
                                           @RequestParam(name="limit", required = false, defaultValue = "3") int limit,
                                           HttpServletRequest request

    ){


        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("recommendFestivals", festivalService.findTop3ByRecommendFestival())
                .build();
    }

    // 부스가져오기
    @GetMapping("{festivalId}/booths")
    public RestEntity getAllBooths(@PathVariable(name="festivalId") int festivalId){
        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("booths", festivalService.getBoothsById(festivalId))
                .build();
    }


}
