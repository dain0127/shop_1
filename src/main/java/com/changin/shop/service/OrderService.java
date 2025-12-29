package com.changin.shop.service;

import com.changin.shop.constant.OrderStatus;
import com.changin.shop.dto.OrderDto;
import com.changin.shop.dto.OrderHistDto;
import com.changin.shop.dto.OrderItemDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
import com.changin.shop.entity.Order;
import com.changin.shop.entity.OrderItem;
import com.changin.shop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;


    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orderList = orderRepository.findOrders(email, pageable);
        Long countOrder = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for(Order order : orderList){
            List<OrderItem> orderItemList = order.getOrderItems();
            OrderHistDto orderHistDto = new OrderHistDto(order);

            for(OrderItem orderItem : orderItemList){
                String imgUrl = itemImgRepository
                        .findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y")
                        .getImgUrl();

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, imgUrl);

                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtoList.add(orderHistDto);
        }


        return new PageImpl<>(orderHistDtoList, pageable, countOrder);
    }


    //하나의 건수를 주문한다.
    public Long order(OrderDto orderDto, String email){
        //call order in this project :
        //((item, member) -> orderItem(reference to item)
        // -> order(reference by item, to member))
        //item
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        //member
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        //orderItem
        OrderItem orderItem = OrderItem.createOrderItem(item,
                 orderDto.getCount().intValue());

        //order (orderItem은 자동으로 추가해 줌.)
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        //임시
        Order order = Order.createOrder(member, orderItemList, UUID.randomUUID().toString());
        orderRepository.save(order);


        //save + remove
        item.removeStock(orderDto.getCount().intValue());

        return order.getId();
    }


    public boolean vaildateOrder(Long orderId, String name) {
        List<Order> orderList = orderRepository.findOrders(name, PageRequest.of(0,1));

        log.info("==============>" + orderList.getFirst().toString());
        if(orderList.isEmpty() || !orderList.getFirst().getId().equals(orderId)){
            return false;
        }else{
            return true;
        }
    }

    public void cancelOrder(Long orderId) {
        //given
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItemList = order.getOrderItems();

        //process
        //stock update
        for(OrderItem orderItem : orderItemList){
            orderItem.getItem().addStock(orderItem.getCount()); //dirty checking.
        }
        //order cancel.
        order.setOrderStatus(OrderStatus.CANCEL);
    }
}
