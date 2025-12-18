package com.changin.shop.dto;

import com.changin.shop.entity.Item;
import com.changin.shop.entity.Order;
import com.changin.shop.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemDto {
    public OrderItemDto(OrderItem orderItem , String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm;
    private int count; //수량
    private int orderPrice; //주문 금액
    private String imgUrl; //이미지 파일
}
