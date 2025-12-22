package com.changin.shop.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDto {
    private Long cartItemId; //장바구니의 상품 아이디.
    private String itemNm;
    private int price;
    private int count;
    private String imgUrl;
}
