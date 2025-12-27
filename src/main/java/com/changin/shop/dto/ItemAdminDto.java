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
public class ItemAdminDto {
    private String categoryNm;

    private Long id;
    private String itemNm; //상품 이름
    private ItemSellStatus itemSellStatus; //아이템 판매 상태
    private String createdBy;
    private LocalDateTime regTime;
    private String imgUrl; //이미지 저장 경로
}
