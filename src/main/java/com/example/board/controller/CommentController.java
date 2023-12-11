package com.example.board.controller;

import com.example.board.dto.CommentDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/spr_board/{boardId}")
    public String comment(@PathVariable Long boardId, HttpServletRequest request, CommentDTO commentDTO){
        HttpSession session = request.getSession(false);
        if(session != null){
            String sessionId = (String)(session.getAttribute("loginId"));
            commentService.insert(boardId,sessionId,commentDTO);

            return "redirect:/board/spr_board/{boardId}";
        }else{
            return "login";
        }
    }
}
