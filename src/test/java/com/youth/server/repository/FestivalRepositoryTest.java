package com.youth.server.repository;

import com.youth.server.domain.Festival;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FestivalRepositoryTest {
    @Autowired FestivalRepository festivalRepository;

    @Test
    void 해당_년도_월에_해당하는_축제_검색() {
        // given
        int year = 2024;
        int month = 5;

        // when
        List<Festival> festivals = festivalRepository.findAllByYearAndMonth(year, month);

        // then
        System.out.println(festivals);
    }

    @Test
    void 즐겨찾기한_사용자가_참여하는_축제_검색() {
        // given
        int userId = 17;

        // when
        List<Festival> festivals = festivalRepository.findAllByFavoriteUserId(userId);

        // then
        System.out.println(festivals.size());
    }
}