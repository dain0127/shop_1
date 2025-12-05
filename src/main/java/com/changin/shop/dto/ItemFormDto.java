package com.changin.shop.dto;


import com.changin.shop.constant.ItemSellStatus;
import jakarta.persistence.*;

//사용자로부터 받아올 데이터
public class ItemFormDto {

    private Long id;

    private String itemNm; //상품 이름

    private int price; //가격

    private int stockNumber; //재고 수량

    private String itemDetail; //상품 상세 설명

    private ItemSellStatus itemSellStatus; //아이템 판매 상태
}
