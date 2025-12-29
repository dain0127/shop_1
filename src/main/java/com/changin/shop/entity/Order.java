package com.changin.shop.entity;

import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Table(name = "orders") //sql 문법 키워드랑 되도록 겹치지 않도록 해야함.
@Entity
@Setter
@Getter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Column(nullable = true, unique = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //외래키로 참조하고 있는 쪽이 JPA에서는 주인.
    //이쪽은 주인이 아님.
    @Builder.Default
    @OneToMany(fetch =  FetchType.LAZY, mappedBy = "order"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();


    public static Order createOrder(Member member, List<OrderItem> orderItems, String orderNumber){
        Order newOrder = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderNumber(orderNumber)
                .orderStatus(OrderStatus.STAY)
                .build();


        newOrder.orderItems.addAll(orderItems);
        orderItems.forEach(orderItem -> {
            orderItem.setOrder(newOrder);
        });

        return newOrder;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public int getTotalPrice(){
        int totalPrice = 0;

        for(OrderItem orderItem : this.orderItems){
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }
}
