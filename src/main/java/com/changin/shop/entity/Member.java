package com.changin.shop.entity;

import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.common.entity.BaseTimeEntity;
import com.changin.shop.constant.Role;
import com.changin.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.changin.shop.entity.QMember.member;

@Entity
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto dto, PasswordEncoder passwordEncoder) {
        Member newMember = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .address(dto.getAddress())
                .role(Role.USER)
                .build();


        return newMember;
    }

    public static Member createAdminMember(MemberFormDto dto, PasswordEncoder passwordEncoder) {
        Member newMember = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .address(dto.getAddress())
                .role(Role.ADMIN)
                .build();


        return newMember;
    }
}
