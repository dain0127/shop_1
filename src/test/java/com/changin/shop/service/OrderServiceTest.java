package com.changin.shop.service;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.constant.OrderStatus;
import com.changin.shop.dto.OrderDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
import com.changin.shop.entity.Order;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.repository.MemberRepository;
import com.changin.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Item saveItem(){
        Item item = Item.builder()
                .itemNm("test")
                .price(1111)
                .itemSellStatus(ItemSellStatus.SELL)
                .itemDetail("test is test")
                .stockNumber(1000)
                .build();

        return itemRepository.save(item);
    }
    private Member saveMember(){
        Member member = Member.builder()
                .email("test@test")
                .build();

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    //Is the total price same?
    public void 주문테스트(){
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(100L);
        orderDto.setItemId(item.getId());

        //when
        Long orderId = orderService.order(orderDto, member.getEmail());


        //then
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        int totalPrice = order.getTotalPrice();

        Assertions.assertEquals(totalPrice, item.getPrice()*100L);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void 주문취소테스트(){
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10L);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());

        //when
        orderService.cancelOrder(orderId);
        Order newOrder = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);;
        Item newItem = itemRepository.findById(item.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        //test restoring stockNumber
        Assertions.assertEquals(item.getStockNumber(), newItem.getStockNumber());
        //test OderStatus transfer to 'CANCEL'
        Assertions.assertEquals(OrderStatus.CANCEL, newOrder.getOrderStatus());
    }
}
