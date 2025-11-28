package com.changin.shop.service;

import com.changin.shop.constant.Role;
import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    public Member createMember() {
        MemberFormDto dto = MemberFormDto.builder()
                .name("changin")
                .email("dain0126@naver.com")
                .password("1234")
                .address("aaa")
                .build();

        return Member.createMember(dto, passwordEncoder);
    }

    @Test
    void saveMemberTest() {
        //given
        Member member = createMember();
        member.setEmail("AAAAA");

        //when
        memberService.saveMember(member);
        memberService.saveMember(member);

        //then


    }
}