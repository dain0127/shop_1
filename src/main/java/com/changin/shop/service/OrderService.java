package com.changin.shop.service;

import com.changin.shop.dto.OrderDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
import com.changin.shop.entity.Order;
import com.changin.shop.entity.OrderItem;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.repository.MemberRepository;
import com.changin.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    //하나의 건수를 주문한다.(장바구니에 넣는다?)
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

        //save
        orderRepository.save(order);

        return order.getId();
    }
}
