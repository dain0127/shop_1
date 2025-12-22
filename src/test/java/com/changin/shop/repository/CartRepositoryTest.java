package com.changin.shop.repository;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.MemberFormDto;
import com.changin.shop.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CartRepositoryTest {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemImgRepository itemImgRepository;
    @Autowired private MemberRepository memberRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String defualtPassword = "1234";
    private final String defualtEmail = "test@test";

    private Member saveMember() {
        MemberFormDto dto = MemberFormDto.builder()
                .name("test")
                .email(defualtEmail)
                .password(defualtPassword)
                .address("aaa")
                .build();

        return Member.createMember(dto, passwordEncoder);
    }
    private Item saveItem(){
        Item newItem = Item.builder()
                .itemNm("test")
                .price(1111)
                .stockNumber(10)
                .itemDetail("it's test")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        return itemRepository.save(newItem);
    }
    private ItemImg saveItemImg(Item item){
        ItemImg newItemImg = ItemImg.builder()
                .imgName("testtesttest.jpg")
                .oriImgName("testImg.jpg")
                .imgUrl("test/test")
                .repImgYn("Y")
                .item(item)
                .build();

        return itemImgRepository.save(newItemImg);
    }
    private Cart createCart(Member member){
        return Cart.builder()
                .member(member)
                .build();
    }
    private CartItem createCartItem(Cart cart, Item item){
        return CartItem.builder()
                .cart(cart)
                .item(item)
                .count(5)
                .build();
    }

    @Test
    @DisplayName("외래키로 연결된 Cart 엔티티 저장 확인")
    public void saveCartTest(){
//        Cart cart = new Cart();
//        Member member = createMember();
//        cart.setMember(member);
//
//        memberRepository.save(member);
//        cartRepository.save(cart);
//
//
//        Member resultMember = memberRepository.findByEmail(defualtEmail)
//                .orElseThrow(EntityNotFoundException::new);
//        Cart resultCart = cartRepository.findById(cart.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertEquals(resultCart.getMember().getId(), resultMember.getId());
    }

    @Test
    @DisplayName("카트 세부 정보 출력 테스트")
    public void findCartDetailDtoListTest(){
        //given
        Member member = saveMember();
        Item item = saveItem();
        ItemImg itemImg = saveItemImg(item);

        Cart cart = cartRepository.save(createCart(member));
        CartItem cartItem = cartItemRepository.save(
                createCartItem(cart, item)
        );

        //when
        List<CartDetailDto> resultList
                = cartItemRepository.findCartDetailDtoList(cart.getId(),
                member.getEmail());

        //then
        CartDetailDto expectedDto = new CartDetailDto(
                cartItem.getId(),
                item.getItemNm(),
                item.getPrice(),
                cartItem.getCount(),
                itemImg.getImgUrl()
        );

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(resultList.getFirst(), expectedDto);
    }

}
