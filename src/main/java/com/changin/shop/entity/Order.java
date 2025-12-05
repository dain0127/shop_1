package com.changin.shop.entity;

import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    //외래키로 참조하고 있는 쪽이 JPA에서는 주인.
    //이쪽은 주인이 아님.
    @OneToMany(fetch =  FetchType.LAZY, mappedBy = "order"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

}
