package com.example.board.service;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void register(MemberDTO memberDTO){
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO){
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());
        if(byMemberId.isPresent()){
            MemberEntity memberEntity = byMemberId.get();
            if(memberEntity.getMemberPw().equals(memberDTO.getMemberPw())){
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public MemberDTO findMemberId(String memberId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberId(memberId);
        if(optionalMemberEntity.isPresent()){
            MemberEntity memberEntity = optionalMemberEntity.get();
            return MemberDTO.toMemberDTO(memberEntity);
        }
        return null;
    }
    public int findMemberVisitCnt(String memberId) {
    //1. 세션에서 받아온 memberId값으로 해당되는 사용자정보(MemberEntity)를 가져옴
    Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberId(memberId);
        if(optionalMemberEntity.isPresent()){
            //2. Optional -> MemberEntity 타입으로 변환
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            return memberDTO.getMemberVisitCnt();
        }
        return 0;
    }

    public void updateVisitCnt(MemberDTO loginResult) {
        //DTO에 있는 visitCnt 1증가
        //증가시킨 DTO값을 -> Entity로
        //Repository memerEntity 값 save
        loginResult.setMemberVisitCnt(loginResult.getMemberVisitCnt()+1);
        MemberEntity memberEntity = MemberEntity.toMemberEntity(loginResult);
        memberRepository.save(memberEntity);
    }

    public void deleteAllEntity() {
        memberRepository.deleteAll();
    }
}
