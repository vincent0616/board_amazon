package com.example.board.controller;


import com.example.board.dto.BoardDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/")
    public String home(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "index";
    }

    @GetMapping("/board/spr_board")
    public String sprBoardForm(Model model, @PageableDefault(page = 1) Pageable pageable){
        Page<BoardDTO> boardDTOPage = boardService.paging(pageable);

        // List<BoardDTO> boardDTOList = boardService.findAll();
        // model.addAttribute("boardList",boardDTOList);

        int startPage = Math.max(1, pageable.getPageNumber() - 2);
        int endPage = Math.min(startPage + 4, boardDTOPage.getTotalPages());

        model.addAttribute("boardList",boardDTOPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "spr_board";
    }

    //게시판 뷰 페이지 이동
    @GetMapping("/board/spr_board/{boardId}")
    public String sprBoardView(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        BoardDTO boardDTO = boardService.findByBoardId(boardId);
        List<CommentDTO> commentDTO = commentService.findByBoardId(boardId);
        boardDTO.setBoardCommentCnt((long)(commentDTO.size()));
        boardService.updateViewCnt(boardId);

        HttpSession session = request.getSession(false);
        boolean sessionChk = false;
        if(session != null){
            sessionChk = true;
        }

        model.addAttribute("commentList",commentDTO);
        model.addAttribute("board",boardDTO);
        model.addAttribute("sessionChk",sessionChk);
        return "spr_board_view";
    }

    @GetMapping("/board/write")
    public String boardWrite(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            return "write";
        }
        return "login";
    }

    @PostMapping("/board/write")
    public String boardWriteForm(@ModelAttribute BoardDTO boardDTO, HttpServletRequest request, MultipartFile file) throws Exception{
        HttpSession session = request.getSession(false);
        String mbId = (String) (session.getAttribute("loginId"));
        boardService.write(boardDTO, mbId, file);
        return "redirect:/board/spr_board";
    }

    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id, HttpServletRequest request){
        BoardDTO boardDTO = boardService.findByBoardId(id);
        HttpSession session = request.getSession(false);
        if(session != null){
            if(session.getAttribute("loginNic").equals(boardDTO.getBoardMemberWriter())){
                boardService.deleteByBoardDTO(boardDTO);
                return "redirect:/board/spr_board";
            }else {
                return "redirect:/board/write";
            }
        }else{
            return "redirect:/board/login";
        }
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, HttpServletRequest request, Model model){
        BoardDTO boardDTO = boardService.findByBoardId(id);
        HttpSession session = request.getSession(false);
        if(session != null){
            if(session.getAttribute("loginNic").equals(boardDTO.getBoardMemberWriter())){
                model.addAttribute("boardList",boardDTO);
                return "update";
            }else {
                return "redirect:/board/write";
            }
        }else{
            return "redirect:/board/login";
        }
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdateForm(@PathVariable Long id, BoardDTO boardDTO, Model model){
        //기존 게시글 내용을 가져오기(id값에 해당하는 게시글)
        BoardDTO exDTO = boardService.findByBoardId(id);
        boardService.update(exDTO,boardDTO);
        model.addAttribute("boardList",exDTO);
        return "redirect:/board/spr_board";
    }

    @GetMapping("/board/spr_board/{id}/like")
    public String boardLike(@PathVariable Long id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            BoardDTO boardDTO = boardService.findByBoardId(id);
            boardService.like(boardDTO);
            return "redirect:/board/spr_board/{id}";
        }else{
            return "login";
        }
    }

    @GetMapping("/board/spr_board/{id}/dislike")
    public String boardDisLike(@PathVariable Long id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            BoardDTO boardDTO = boardService.findByBoardId(id);
            boardService.disLike(boardDTO);
            return "redirect:/board/spr_board/{id}";
        }else{
            return "login";
        }
    }




}
