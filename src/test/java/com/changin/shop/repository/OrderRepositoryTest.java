package com.changin.shop.repository;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager em;


    private final String defualtPassword = "1234";
    private final String defualtEmail = "test@test";

    public Item createItem() {
        Item item = Item.builder()
                .itemNm("aaaa")
                .price(123)
                .stockNumber(111)
                .itemDetail("hello")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        return item;
    }


    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        //given
        Order order = new Order();

        for(int i=0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);

            order.getOrderItems().add(orderItem);
        }

        //when
        orderRepository.saveAndFlush(order);
        em.clear();


        //then
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아 삭제 테스트")
    public void orphanRemovalTest(){
        //given
        Order order = new Order();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);

        order.getOrderItems().add(orderItem);

        orderRepository.saveAndFlush(order);
        em.clear();

        //when
        Order savedOrder = orderRepository.findById(order.getId())
                        .orElseThrow(EntityNotFoundException::new);
        savedOrder.getOrderItems().removeFirst();
        em.flush();

        //then
        assertEquals(0, savedOrder.getOrderItems().size());


    }

//    @Test
//    @DisplayName("지연 로딩 테스트")
//    public void lazyLoadingTest(){
//        //given
//        Order order = new Order();
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setOrder(order);
//
//        order.getOrderItems().add(orderItem);
//
//        orderRepository.saveAndFlush(order);
//        em.clear();
//    }

}
