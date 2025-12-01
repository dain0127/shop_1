package com.changin.shop.controller;


import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원 가입 페이지로 이동
    @GetMapping("/member/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }


    //회원 가입 처리
    @PostMapping("/member/new")
    public String insertMember(@Valid  MemberFormDto memberFormDto,
                               BindingResult bindingResult,
                               Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try{
            //log.info("======================> memberFormDto : " + memberFormDto);
            memberService.saveMember(Member.createMember(memberFormDto, passwordEncoder));
        }catch(IllegalStateException e){
            log.info(e.getMessage());
            e.getStackTrace();

            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }

        return "redirect:/";
    }


    //로그인 페이지로 전송
    @GetMapping("/member/login")
    public String memberLoginForm(Model model){
        return "member/memberLoginForm";
    }




}
