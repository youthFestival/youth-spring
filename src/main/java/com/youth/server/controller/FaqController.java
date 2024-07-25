package com.youth.server.controller;


import com.youth.server.domain.FAQ;
import com.youth.server.repository.FaqRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/faq")
public class FaqController {

    private FaqRepository faqRepository;

    @GetMapping
    public List<FAQ> getAllFaq(){
        return faqRepository.findAll();
    }

}
