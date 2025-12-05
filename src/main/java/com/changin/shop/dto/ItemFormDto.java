package com.changin.shop.dto;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//사용자로부터 받아올 데이터
public class ItemFormDto {

    private Long id;

    private String itemNm; //상품 이름

    private int price; //가격

    private int stockNumber; //재고 수량

    private String itemDetail; //상품 상세 설명

    private ItemSellStatus itemSellStatus; //아이템 판매 상태

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> itemImgId = new ArrayList<>();


    @Autowired
    private static ModelMapper mapper;

    public static ItemFormDto entityToDto(Item item) {
        return mapper.map(item, ItemFormDto.class);
    }

    public Item toEntity(){
        return mapper.map(this, Item.class);
    }
}
