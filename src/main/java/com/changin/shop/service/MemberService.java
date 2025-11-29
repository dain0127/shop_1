package com.changin.shop.service;


import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j //log찍어주는 라이브러리
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    final private MemberRepository memberRepo;

    public void validateDuplicationMember(Member member) {
        if(memberRepo.findByEmail(member.getEmail()).isPresent()){
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
    }

    public Member saveMember(Member member) {
        //try {
            validateDuplicationMember(member);
            return memberRepo.save(member);
//        }catch (Exception e){
//            log.info(e.getMessage());
//            e.getStackTrace();
//            return null;
//        }
    }

}
