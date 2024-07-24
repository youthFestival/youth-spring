package com.youth.server.service;

import com.youth.server.domain.Artist;
import com.youth.server.domain.Festival;
import com.youth.server.domain.Image;
import com.youth.server.dto.festival.LineUpDTO;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public List<Festival> findAllByYearAndMonth(int year, int month) {
        return festivalRepository.findAllByYearAndMonth(year, month);
    }

//    public List<Festival> findFestival(FestivalRequest data) {
//        return festivalRepository.findFestival(data);
//    }

    public Festival findFestivalById(int festivalId) {
        Optional<Festival> festival = festivalRepository.findFestivalById(festivalId);
        if (festival.isEmpty()) {
            throw new NotFoundException("해당 축제가 존재하지 않습니다.");
        }
        return festival.get();
    }

    public List<LineUpDTO> getFestivalLineUpById(int festivalId) {
        Festival festival = findFestivalById(festivalId);
        Set<Artist> participatingArtists = festival.getParticipatingArtists();

        if (participatingArtists.isEmpty()) {
            throw new NotFoundException("해당 축제에 참가하는 아티스트가 존재하지 않습니다.");
        }

        List<LineUpDTO> lineUpDTOList = new ArrayList<>();

        participatingArtists.stream()
                .forEach(artist -> {
                    Image artistProfileUrl = artist.getArtistProfileImage();

                    // artistProfileUrl가 null이면 return
                    if(artistProfileUrl == null) {
                        return;
                    }

                    lineUpDTOList.add(new LineUpDTO(artist.getName(),artistProfileUrl.getImgUrl() ));
                });


        return lineUpDTOList;

    }


}
