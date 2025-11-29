package com.changin.shop.service;

import com.changin.shop.constant.Role;
import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
    @DisplayName("중복 회원 예외 발생 테스트")
    void saveMemberTest2() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        //when
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> memberService.saveMember(member2));

        //then
        assertEquals("중복된 이메일입니다.", e.getMessage());
    }

    @Test
    void saveMemberTest1() {
        //given
        Member member = createMember();
        //when
        Member member1 = memberService.saveMember(member);
        //then
        System.out.println("================member : " + member);
        System.out.println("=====================member1 : " + member1);
    }
}