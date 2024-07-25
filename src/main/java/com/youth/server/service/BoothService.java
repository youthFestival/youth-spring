package com.youth.server.service;

import com.youth.server.domain.Booth;
import com.youth.server.domain.Item;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;


    public Set<Item> findBoothItemById(int boothId) {
        Optional<Booth> booth = boothRepository.findById(boothId);

        if (booth.isEmpty()) {
            throw new NotFoundException("해당 부스가 존재하지 않습니다.");
        }

        return booth.get().getItems();

    }

}
