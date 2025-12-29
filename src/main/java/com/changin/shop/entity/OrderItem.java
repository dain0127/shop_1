package com.changin.shop.entity;


import com.changin.shop.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; //주문 금액
    private int count; //수량


    //Item별 주문 내역 생성
    public static OrderItem createOrderItem(Item item, int count){
        return OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(count)
                .build();
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }
}
