package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/board/login")
    public String loginForm(){
        return "login";
    }

//    @PostMapping("/board/login")
//    public String login(@ModelAttribute MemberDTO memberDTO, HttpServletRequest httpServletRequest, BindingResult bindingResult){
//        MemberDTO loginResult = memberService.login(memberDTO);
//        if(loginResult != null){
//            httpServletRequest.getSession().invalidate();
//            HttpSession session = httpServletRequest.getSession(true);
//            session.setAttribute("loginNic", loginResult.getMemberNic());
//            session.setAttribute("loginGd", loginResult.getMemberGd());
//            session.setAttribute("loginId",loginResult.getMemberId());
//
//            //로그인 할때마다 방문횟수 1씩 증가
//            memberService.updateVisitCnt(loginResult);
//            return "redirect:/";
//        }else{
//            bindingResult.reject("loginFail","아이디 또는 비밀번호가 틀렸습니다.");
//        }
//
//        if(bindingResult.hasErrors()){
//            return "login";
//        }
//        return "login";
//    }

    @GetMapping("/board/logout")
    public String logout(HttpServletRequest request){
        //
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/board/register")
    public String regForm(){
        return "register";
    }

    @PostMapping("/board/register")
    public String register(@ModelAttribute MemberDTO memberDTO){
        memberService.register(memberDTO);
        return "index";
    }

    @GetMapping("/board/mypage")
    public String mypage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            String memberId = (String)session.getAttribute("loginId");
            String memberNic = (String)session.getAttribute("loginNic");
            MemberDTO memberDTO = memberService.findMemberId(memberId);
            List<BoardDTO> boardDTOList = boardService.findByBoardWriter(memberNic);
            int boardWriteCnt = boardService.findByBoardWriteCnt(memberNic);
            int commentWriteCnt = commentService.findByCommentWriteCnt(memberNic);

            //방문횟수 가져오기
            int memberVisitCnt = memberService.findMemberVisitCnt(memberId);

            model.addAttribute("visitCnt",memberVisitCnt);
            model.addAttribute("commentWriteCnt",commentWriteCnt);
            model.addAttribute("writeCnt",boardWriteCnt);
            model.addAttribute("writeList",boardDTOList);
            model.addAttribute("member",memberDTO);
        }
        return "mypage";
    }

//    @PostMapping("/board/mypage_update")
//    public String mypageUpdate(Model model, MemberDTO memberDTO, HttpServletRequest request){
//        HttpSession session = request.getSession(false);
//        memberDTO
//        model.addAttribute(,memberId)
//    }

}
