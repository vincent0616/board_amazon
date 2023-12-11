package com.example.board.entity;

import com.example.board.dto.CommentDTO;
import com.example.board.member.MemberGrade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "comment_table")
public class CommentEntity{
//    댓글ID,  댓글내용, 작성자, 댓글작성시간, 게시글번호,  등급

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String commentContent;

    @ManyToOne
    @JoinColumn(name="commentMemberWriter", referencedColumnName = "memberNic")
    private MemberEntity memberWriter;
    @Column
    @CreationTimestamp
    private Date commentDate;
    @ManyToOne
    @JoinColumn(name = "commentBoardNum")
    private BoardEntity boardNumber;


    public static CommentEntity toCommentEntity(CommentDTO commentDTO){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentId(commentDTO.getCommentId());
        commentEntity.setCommentContent(commentDTO.getCommentContent());
        commentEntity.setCommentDate(commentDTO.getCommentDate());
        commentEntity.setBoardNumber(commentDTO.getBoardNumber());
        commentEntity.setMemberWriter(commentDTO.getMemberWriter());

        return commentEntity;
    }

}
