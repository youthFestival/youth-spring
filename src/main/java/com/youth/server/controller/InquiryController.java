package com.youth.server.controller;


import com.youth.server.domain.Inquiry;
import com.youth.server.dto.InquiryDTO;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.InquiryRepository;
import com.youth.server.util.Const;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryRepository inquiryRepository;
    private final JwtUtil jwtUtil;

    /**
     * 내 문의 글 또는 해당 유저 가져오기
     */

    @GetMapping
    public RestEntity getInquiriesByParams(@RequestParam(name = "userId",required = false) Integer uid,
                                           @RequestParam(name = "festivalId", required = false) Integer festivalId,
                                     @RequestParam(name="page", required = false, defaultValue = "0") Integer page,
                                     HttpServletRequest request){

        List<Inquiry> inquiries = null;
        PageRequest pageRequest = PageRequest.of(page, Const.DEAFULT_LIMIT);


        if(uid == null && festivalId == null){
            inquiries = inquiryRepository.findAllQuestion(pageRequest);
        }

        if(uid != null){
            inquiries = inquiryRepository.findAllQuestionByUserId(uid,pageRequest);
        }

        if(festivalId != null){
            inquiries = inquiryRepository.findAllQuestionByFestivalId(festivalId,pageRequest);
        }

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("조회 성공")
                .put("inquiries", inquiries)
                .build();

    }

    /**
     * 문의 글 수정하기
     */

    @PutMapping("/{inquiryId}")
    public RestEntity updateInquiry(@PathVariable(name="inquiryId",required = true) Integer inquiryId,
                                    @RequestBody HashMap<String,String> payload,
                                    HttpServletRequest request) {

        //@TODO 권한 확인

        Optional<Inquiry> inquiry = inquiryRepository.findById(inquiryId);

        // 값 바뀌기전 기본값.
        if(inquiry.isEmpty()){
            throw new NotFoundException("해당 문의글이 존재하지 않습니다.");
        }


        String title = payload.get("title");
        String content = payload.get("content");

        Inquiry toUpdateInquiry = inquiry.get();

        if(!("".equals(title))) toUpdateInquiry.setTitle(title);
        if(!("".equals(content))) toUpdateInquiry.setContent(content);


        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("수정되었습니다.")
                .put("inquiry", inquiryRepository.save(toUpdateInquiry))
                .build();
    }


    /***
     * 문의 글 삭제하기
     */
    @DeleteMapping("/{inquiryId}")
    public RestEntity deleteInquiry(@PathVariable(name="inquiryId"
            ,required = true) Integer inquiryId,
                                    HttpServletRequest request) {

        //@TODO 권한확인

        Optional<Inquiry> inquiry = inquiryRepository.findById(inquiryId);
        if(inquiry.isEmpty()) throw new NotFoundException("해당 문의글이 존재하지 않습니다.");

        inquiryRepository.delete(inquiry.get());

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("삭제되었습니다.")
                .build();
    }

    /**
     * 문의글 상세보기
     */

    @GetMapping("/{inquiryId}")
    public RestEntity getDetailOfInquiry(@PathVariable int inquiryId) {
        return RestEntity.builder()
                .message("조회되었습니다.")
                .put("inquiry",inquiryId)
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping
    public RestEntity createInquiry(HttpServletRequest request,
                                    @RequestBody InquiryDTO newInquiry) {

        // @TODO 권한 확인
        Optional<String> username  = jwtUtil.getUserId(request);

        return RestEntity.builder().build();
    }

}
