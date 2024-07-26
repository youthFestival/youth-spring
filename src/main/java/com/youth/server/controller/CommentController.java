package com.youth.server.controller;

import com.youth.server.domain.Comment;
import com.youth.server.domain.Festival;
import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.exception.PermissionDeniedException;
import com.youth.server.service.CommentService;
import com.youth.server.service.FestivalService;
import com.youth.server.service.UserService;
import com.youth.server.util.Const;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    @GetMapping("{festivalId}")
    public RestEntity getComments(@RequestParam(name = "limit", required = false) Integer limit,
                                  @RequestParam(name = "offset", required = false) Integer offset,
                                  @PathVariable(name = "festivalId") int festivalId,
                                  HttpServletRequest request
    ) {

        if (limit == null || limit <= 0) limit = Const.DEAFULT_LIMIT;
        if (offset == null || offset <= 0) offset = Const.DEAFULT_OFFSET;


        int page = offset == 0 ? 0 : offset / limit;

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("댓글 조회 성공")
                .put("comments", commentService.getCommentsByFestivalId(festivalId, PageRequest.of(page, limit)))
                .build();
    }

    @GetMapping("/my-comment")
    public RestEntity getMyComments(HttpServletRequest request) {

        Optional<String> currentUserId = jwtUtil.getUserId(request);
        if (currentUserId.isEmpty()) throw new PermissionDeniedException("로그인이 필요합니다.");

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("댓글 조회 성공")
                .put("comments", commentService.getCommentsByUserId(currentUserId.map(Integer::parseInt).get()))
                .build();
    }

    // 댓글 생성
    @PostMapping
    public RestEntity createComment(HttpServletRequest request, @RequestBody Map<String, String> payload) {

        User currentUser  = jwtUtil.getUserId(request)
                .map(userService::findByUserId)
                .orElseThrow(()-> new PermissionDeniedException("로그인이 필요합니다."));

        // 댓글 작성할 페스티벌 객체 가져오기
        Festival festival = Optional.of(payload.get("festivalId")).map(Integer::parseInt)
                .map(festivalService::findFestivalById)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 해당하는 페스티벌을 찾을 수 없습니다."));


        // 작성하는 유저 정보 가져오기
        Comment newComment = Comment.builder()
                .festival(festival)
                .author(currentUser)
                .content(payload.get("content"))
                .build();


        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("댓글 생성 성공")
                .put("comment", commentService.join(newComment))
                .build();
    }


}
