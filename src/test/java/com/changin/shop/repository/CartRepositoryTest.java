package com.changin.shop.repository;


import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
//@Transactional
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final String defualtPassword = "1234";
    private final String defualtEmail = "test@test";

    public Member createMember() {
        MemberFormDto dto = MemberFormDto.builder()
                .name("test")
                .email(defualtEmail)
                .password(defualtPassword)
                .address("aaa")
                .build();

        return Member.createMember(dto, passwordEncoder);
    }

    @Test
    @DisplayName("외래키로 연결된 Cart 엔티티 저장 확인")
    public void saveCartTest(){
//        Cart cart = new Cart();
//        Member member = createMember();
//        cart.setMember(member);
//
//        memberRepository.save(member);
//        cartRepository.save(cart);
//
//
//        Member resultMember = memberRepository.findByEmail(defualtEmail)
//                .orElseThrow(EntityNotFoundException::new);
//        Cart resultCart = cartRepository.findById(cart.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertEquals(resultCart.getMember().getId(), resultMember.getId());
    }

}
