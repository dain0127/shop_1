package com.changin.shop.repository;


import com.changin.shop.entity.Cart;
import com.changin.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    public CartItem findByCartId(Long cartId);
    public CartItem findByItemId(Long itemId);
}
