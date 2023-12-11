package com.example.board.entity;

import com.example.board.dto.MemberDTO;
import com.example.board.member.MemberGrade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="member_table")
public class MemberEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name="memberId")
    private String memberId;

    @Column
    private String memberPw;

    @Column(unique = true)
    private String memberNic;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberGrade memberGd;

    @Column(columnDefinition = "int default 0")
    private int memberVisitCnt;

    @OneToMany(mappedBy = "memberWriter")
    private List<CommentEntity> writers = new ArrayList<>();

    @OneToMany(mappedBy = "boardMemberWriter")
    private List<BoardEntity> boardWriters = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberPw(memberDTO.getMemberPw());
        memberEntity.setMemberNic(memberDTO.getMemberNic());
        memberEntity.setMemberGd(MemberGrade.Bronze);
        memberEntity.setMemberVisitCnt(memberDTO.getMemberVisitCnt());

        return  memberEntity;
    }

}
