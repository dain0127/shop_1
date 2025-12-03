package com.changin.shop.repository;


import com.changin.shop.entity.Cart;
import com.changin.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
