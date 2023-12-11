package com.example.board.member;

import lombok.Getter;
import lombok.Setter;

public enum MemberGrade {
    Bronze("브론즈"),
    Silver("실버"),
    Gold("골드");

    @Setter
    private String krName;


    MemberGrade(String krname){
        this.krName = krname;
    }

    public String getKrName(){
        return krName;
    }
}
