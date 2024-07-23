package com.youth.server.service;

import com.youth.server.domain.Festival;
import com.youth.server.repository.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    @Autowired private FestivalRepository festivalRepository;

    public List<Festival> findAllByYearAndMonth(int year, int month) {
        return festivalRepository.findAllByYearAndMonth(year, month);
    }
}
