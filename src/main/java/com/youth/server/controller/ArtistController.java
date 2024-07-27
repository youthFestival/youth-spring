package com.youth.server.controller;

import com.youth.server.domain.Artist;
import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.ArtistRepository;
import com.youth.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/artist")
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    @GetMapping
    public RestEntity searchArtistWithProfile(@RequestParam(required = false) Integer uid,
                                              @RequestParam(required = false) String search){

        List<Artist> artists = null ;


        if(uid == null && search == null) {
            artists = artistRepository.findAll();
        }

        else if(uid == null){
            artists = artistRepository.findAllByNameContains(search);
        }

        else {
            artists = userRepository.findById(uid).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없어요."))
                    .getFavoriteArtists()
                    .stream().filter(artist -> {
                if(search == null){
                    return  true;
                }
                return artist.getName().contains(search);
            }).toList();
        }

        return RestEntity.builder()
                .message("아티스트 정보가 조회되었습니다.")
                .put("artists", artists)
                .status(HttpStatus.OK)
                .build();
    }


    @PostMapping()
    public RestEntity addOrRemoveAristToWishList(@RequestBody HashMap<String,Integer> payload){

        Integer uid = payload.get("userId");
        Integer artistId =  payload.get("artistId");

        User user = userRepository.findById(uid).orElseThrow(()-> new NotFoundException("유저가 존재하지 않음."));
        Artist artist = artistRepository.findById(artistId).orElseThrow(()-> new NotFoundException("아티스트가 존재하지 않음."));

        List<Artist> artistList = user.getFavoriteArtists();
        Set<User> userList = artist.getSubscribedUsers();

        // 삭제 절차
        boolean isAlreadyAdded = artistList.contains(artist);
        if(isAlreadyAdded){
            artistList.remove(artist);
            userList.remove(user);
        }

        // 추가 절차
        else {
            artistList.add(artist);
            userList.add(user);
        }

        artistRepository.save(artist);
        userRepository.save(user);

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message(isAlreadyAdded ? "삭제하였습니다" : "추가되었습니다.")
                .put("userFavoriteList", artistList)
                .build();
    }

}
