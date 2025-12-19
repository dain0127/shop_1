package com.changin.shop.entity;

import com.changin.shop.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id", nullable = false)
    Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id", nullable = false)
    Item item;

    int count;

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setItem(item);
        newCartItem.setCount(count);

        return newCartItem;
    }

    public void addCount(int addCount){
        this.count+=addCount;
    }
}
