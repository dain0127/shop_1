package com.changin.shop.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartOrderDto {
    private String orderNumber; //order 고유번호
    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;
}
