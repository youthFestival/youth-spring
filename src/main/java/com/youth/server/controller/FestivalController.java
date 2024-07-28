package com.youth.server.controller;

import com.youth.server.domain.Festival;
import com.youth.server.domain.Image;
import com.youth.server.domain.User;
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
import java.util.*;

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
                                   @RequestParam(name= "category", required = false) String category,
                                   @RequestParam(name= "search", required = false) String search,
                                   @RequestParam(name= "isFavorite", required = false, defaultValue = "false") Boolean isFavorite,
                                   @RequestParam(name= "isOngoing", required = false) Boolean isOngoing,
                                   HttpServletRequest request
    ){
        FestivalRequest filter = FestivalRequest.builder().limit(limit).offset(offset).sortBy(sortBy).locality(locality).category(category).search(search).isFavorite(isFavorite).build();


        // 로그인 여부 확인 (isFavorite)
        Optional<Integer> currentUid = jwtUtil.getUid(request);
        List<Festival> favoritesFestival = currentUid.map(festivalService::findFavoriteFestivalByUid).orElse(new ArrayList<>());

        List<Integer> favoriteFestivalIds = new ArrayList<>();

        for(Festival festival : favoritesFestival) {
            favoriteFestivalIds.add(festival.getId());
        }

        // 검색 조건에 맞는 축제 조회
        PageRequest pageRequest = PageRequest.of(
                filter.getOffset() == 0 ? 0 :
                        filter.getOffset() / filter.getLimit()
        , filter.getLimit());

        List<SearchFestivalByFilterDTO> filteredFestivals = festivalService.findFestival(filter,pageRequest);
        Comparator<SearchFestivalByFilterDTO> comparator = null;

    // 정렬
//        @TODO 정렬


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
                .filter(image -> image.getCategory().equals(Image.Category.썸네일))
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
                .put("showTime", Duration.between(festival.getStageOpen(),festival.getStageClose()).toMinutes()+"분")
                .put("category", festival.getCategory())
                .put("organizer", festival.getOrganizer())
                .put("minAge", festival.getMinAge())
                .put("tel",festival.getTel())
                .put("organizerUrl", festival.getOrganizerUrl())
                .put("geoLocation", festival.getGeolocation())
                .put("locality", festival.getLocality())
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

    /**
     * 찜목록에 추가
     * @return
     */
    @PutMapping("{festivalId}/like")
    public RestEntity addWishlist (@PathVariable(name="festivalId") Integer festivalId, @CookieValue(name = Const.AUTH_TOKEN_NAME) String token) {
        String userId = jwtUtil.getValueOf(token, "userId");

        User currentUser = userService.findByUserId(userId);
        Festival currentFestival = festivalService.findFestivalById(festivalId);
        RestEntity.RestEntityBuilder response = RestEntity.builder();

        response.put("userId",currentUser.getUserId());

        // 이미 추가되어있는 경우
        if(currentFestival.getFavoriteUsers().contains(currentUser)){
            currentFestival.getFavoriteUsers().remove(currentUser);
            currentUser.getFavoriteFestivals().remove(currentFestival);

            response.message("찜을 취소했습니다.");
        }else{
            currentFestival.getFavoriteUsers().add(currentUser);
            currentUser.getFavoriteFestivals().add(currentFestival);
            response.message("찜을 추가했습니다.");
        }

        festivalService.save(currentFestival);
        userService.save(currentUser);

        response.put("likeCount" , currentFestival.getFavoriteUsers().size());


        return response.build();

    }

    /**
     * 찜목록 조회
     * /api/festival/:festivalId/like
     *
     */
    @GetMapping("{festivalId}/like")
    public RestEntity getFavoriteCount(@PathVariable("festivalId") Integer festivalId){
        Set<User> favoriteUsers = festivalService.findFestivalById(festivalId).getFavoriteUsers();
        return RestEntity.builder()
                .status(HttpStatus.OK)
                .put("likeCount", favoriteUsers.size())
                .put("favoriteUser", favoriteUsers)
                .message("조회되었습니다.")
                .build();

    }
}
