package com.youth.server.controller;

import com.youth.server.domain.Comment;
import com.youth.server.domain.Festival;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.exception.PermissionDeniedException;
import com.youth.server.service.CommentService;
import com.youth.server.service.FestivalService;
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

    @PostMapping
    public RestEntity createComment(HttpServletRequest request, @RequestBody Map<String, String> payload) {


        Optional<String> currentUserId = jwtUtil.getUserId(request);
        if (currentUserId.isEmpty()) throw new PermissionDeniedException("로그인이 필요합니다.");

        Comment comment = new Comment();
        Festival festival = Optional.of(payload.get("festivalId")).map(Integer::parseInt)
                .map(festivalService::findFestivalById)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 해당하는 페스티벌을 찾을 수 없습니다."));

        comment.setFestival(festival);
        comment.setContent(payload.get("content"));

        commentService.join(comment);

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("댓글 생성 성공")
                .put("comment", commentService.createComment(null))
                .build();
    }
}
