package com.changin.shop.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberFormDto {
    private String name;
    private String email;
    private String password;

    private String address;
}
