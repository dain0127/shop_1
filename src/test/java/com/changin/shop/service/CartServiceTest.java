package com.changin.shop.service;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.entity.CartItem;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
import com.changin.shop.repository.CartItemRepository;
import com.changin.shop.repository.CartRepository;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepositroy;
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
    private Cart saveCart(Member member){
        Cart newCart = Cart.builder()
                .member(member)
                .build();

        cartRepository.save(newCart);
        return newCart;
    }

    @Test
    @DisplayName("카트에 아이템 추가 테스트")
    public void 카트추가(){
        //given
        Item item = saveItem();
        Member member = saveMember();
        final int count = 10;
        CartItemDto cartItemDto = new CartItemDto(item.getId(), count);

        //when
        Long cartId = cartService.addCart(cartItemDto, member.getEmail());

        //then
        CartItem newCartItem = cartItemRepositroy.findByCartId(cartId);
        Assertions.assertEquals(cartId, newCartItem.getCart().getId());
        Assertions.assertEquals(item.getId(), newCartItem.getItem().getId());
        Assertions.assertEquals(count, newCartItem.getCount());
    }
}
