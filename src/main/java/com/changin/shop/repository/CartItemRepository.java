package com.changin.shop.repository;


import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long>, CartItemRepositoryCustom{
    public List<CartItem> findByCartId(Long cartId);
    public CartItem findByItemId(Long itemId);
}
