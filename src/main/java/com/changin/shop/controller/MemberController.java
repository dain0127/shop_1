package com.changin.shop.controller;


import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.service.MemberService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String LoginForm(){
        return "member/memberLoginForm";
    }

    //로그인 페이지로 전송
    @GetMapping("/member/login/error")
    public String LoginError(Model model){
        model.addAttribute("loginErrorMsg"
                ,"아이디 또는 비밀번호가 일치하지 않습니다.");

        return "member/memberLoginForm";
    }

    //로그아웃 페이지로 전송
    @GetMapping("/member/logout")
    public String LogoutForm(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/member/login";
    }

}
