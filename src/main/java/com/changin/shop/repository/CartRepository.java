package com.changin.shop.repository;


import com.changin.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    public Cart findByMemberId(Long memberId);
}
