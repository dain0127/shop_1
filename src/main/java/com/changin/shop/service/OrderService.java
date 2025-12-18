package com.changin.shop.service;

import com.changin.shop.dto.OrderDto;
import com.changin.shop.dto.OrderHistDto;
import com.changin.shop.dto.OrderItemDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
import com.changin.shop.entity.Order;
import com.changin.shop.entity.OrderItem;
import com.changin.shop.repository.ItemImgRepository;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.repository.MemberRepository;
import com.changin.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
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

        //order
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);

        //save + remove
        orderRepository.save(order);
        item.removeStock(orderDto.getCount().intValue());

        return order.getId();
    }
}
