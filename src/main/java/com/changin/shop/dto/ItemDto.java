package com.changin.shop.dto;


import com.changin.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    public static int TestVariable = 0;

    private Long id;

    private String itemNm; //상품 이름

    private int price; //가격

    private int stockNumber; //재고 수량

    private String itemDetail; //상품 상세 설명

    //entity와 달리 문자열로 처리.
    private String itemSellStatus; //아이템 판매 상태

    private LocalDateTime regTime; // 상품 등록 날짜

    private LocalDateTime updateTime; // 상품 수정 날짜
}
