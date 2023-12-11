package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@DynamicInsert
@Table(name = "board_table")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column
    private boolean boardCategory;

    @ManyToOne
    @JoinColumn(name = "boardWriter", referencedColumnName = "memberNic")
    private MemberEntity boardMemberWriter;

    @Column
    @CreationTimestamp
    private Date boardDate;

    @Column
    private String boardTitle;

    @Column
    private String boardContent;

    @Column
    private Long boardViewCnt;

    @Column
    private Long boardLike;

    @Column
    private Long boardDislike;

    @Column
    private Long boardCommentCnt;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @OneToMany(mappedBy = "boardNumber")
    private List<CommentEntity> boardNum = new ArrayList<>();

    public static BoardEntity toBoardEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardDTO.getBoardId());
        boardEntity.setBoardCategory(boardDTO.isBoardCategory());
        boardEntity.setBoardMemberWriter(boardDTO.getBoardMemberWriter());
        boardEntity.setBoardDate(boardDTO.getBoardDate());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContent(boardDTO.getBoardContent());
        boardEntity.setBoardViewCnt(boardDTO.getBoardViewCnt());
        boardEntity.setBoardLike(boardDTO.getBoardLike());
        boardEntity.setBoardDislike(boardDTO.getBoardDislike());
        boardEntity.setBoardCommentCnt(boardDTO.getBoardCommentCnt());
        boardEntity.setFileName(boardDTO.getFileName());
        boardEntity.setFilePath(boardDTO.getFilePath());

        return boardEntity;
    }

}
