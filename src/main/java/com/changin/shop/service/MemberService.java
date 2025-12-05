package com.changin.shop.service;


import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j //log찍어주는 라이브러리
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    @Autowired
    final private MemberRepository memberRepo;

    public void validateDuplicationMember(Member member) throws IllegalStateException {
        if(memberRepo.findByEmail(member.getEmail()).isPresent()){
            throw new IllegalStateException("중복된 이메일입니다.");
        }
    }

    public Member saveMember(Member member) throws IllegalStateException {
        validateDuplicationMember(member);
        return memberRepo.save(member);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " : 해당 사용자가 없습니다."));

        log.info("==============> member of that email : " + member);

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
