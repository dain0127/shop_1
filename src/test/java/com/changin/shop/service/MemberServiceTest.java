package com.changin.shop.service;

import com.changin.shop.constant.Role;
import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc; //테스트에 필요한 기능만 가지는 가짜 객체


    private final String defualtPassword = "1234";


    public Member createMember() {
        MemberFormDto dto = MemberFormDto.builder()
                .name("changin")
                .email("111@111")
                .password(defualtPassword)
                .address("aaa")
                .build();

        return Member.createMember(dto, passwordEncoder);
    }

    @Test
    @DisplayName("관리자 로그인 성공 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print()) //콘솔 창에 띄우기
                .andExpect(status().isOk()); //해당 url을 아직 만들지 않았음.

    }

    @Test
    @DisplayName("관리자 로그인 성공 테스트")
    @WithMockUser(username = "user", roles = "USER")
    void userLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }



    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest() throws Exception {
        Member member = createMember();
        memberService.saveMember(member); //암호화된 password가 db에 저장

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/member/login")
                .user(member.getEmail()).password(defualtPassword)) //평문
                .andExpect(SecurityMockMvcResultMatchers.authenticated());

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