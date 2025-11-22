package com.changin.shop.entity;


import com.changin.shop.constant.ItemSellStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm; //상품 이름


    private int price; //가격

    private int stockNumber; //재고 수량

    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    private ItemSellStatus itemSellStatus; //아이템 판매 상태

    private LocalDateTime regTime; // 상품 등록 날짜

    private LocalDateTime updateTime; // 상품 수정 날짜
}
