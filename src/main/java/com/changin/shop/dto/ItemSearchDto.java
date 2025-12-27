package com.changin.shop.dto;

import com.changin.shop.constant.ItemSellStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSearchDto {

    private String searchDateType;              // 검색 기준일. all, 1d, 1w, 1m, 6m

    private ItemSellStatus searchSellStatus;    // 판매 상태. SELL, SOLD_OUT

    private String searchBy;                    // 검색 조건. itemNM, createdBy

    private String searchQuery;                 // 검색어. keyword.

    private Long categoryId;                    //카테고리 id
}