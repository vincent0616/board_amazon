package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.entity.MemberEntity;
import com.example.board.member.MemberGrade;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void insert(Long boardId, String sessionId, CommentDTO commentDTO) {
        MemberEntity me = memberRepository.findByMemberId(sessionId).orElse(null);
        commentDTO.setBoardNumber(boardRepository.findById(boardId).orElse(null));
        commentDTO.setMemberWriter(me);

        commentRepository.save(CommentEntity.toCommentEntity(commentDTO));
    }


    public List<CommentDTO> findByBoardId(Long boardId) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardNumber_BoardId(boardId);
        List<CommentDTO> commentDTOS = new ArrayList<>();

        for(CommentEntity commentEntity : commentEntityList){
            commentDTOS.add(CommentDTO.toCommentDTO(commentEntity));
        }

        return commentDTOS;
    }

    public int findByCommentWriteCnt(String memberNic) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByMemberWriter_MemberNic(memberNic);
        return commentEntityList.size();
    }
}
