package com.changin.shop.service;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.entity.*;
import com.changin.shop.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CartServiceTest {

    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepositroy;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemImgRepository itemImgRepository;
    @Autowired private MemberRepository memberRepository;


    private Item saveItem(String itemName){
        Item item = Item.builder()
                .itemNm("test : " + itemName)
                .price(1111)
                .itemSellStatus(ItemSellStatus.SELL)
                .itemDetail("test is test")
                .stockNumber(1000)
                .build();

        return itemRepository.save(item);
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
    private CartItem saveCartItem(Cart cart, Item item){
        CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(5)
                .build();

        return cartItemRepositroy.save(newCartItem);
    }

    @Test
    @DisplayName("카트에 아이템 추가 테스트")
    public void 카트추가(){
        //given
        Item item = saveItem("");
        Member member = saveMember();
        final int count = 10;
        CartItemDto cartItemDto = new CartItemDto(item.getId(), count);

        //when
        Long cartId = cartService.addCart(cartItemDto, member.getEmail());

        //then
        CartItem newCartItem = cartItemRepositroy.findByCartId(cartId).getFirst();
        Assertions.assertEquals(cartId, newCartItem.getCart().getId());
        Assertions.assertEquals(item.getId(), newCartItem.getItem().getId());
        Assertions.assertEquals(count, newCartItem.getCount());
    }

    @Test
    @DisplayName("카트 리스트 출력 테스트")
    //@WithMockUser(username = "test@test", password = "test")
    public void 카트리스트출력(){
        //given
        Member member = saveMember();
        Cart cart = saveCart(member);
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Item item = saveItem(""+i);
            saveItemImg(item);
            saveCartItem(cart, item);

            itemList.add(item);
        }

        Long cartId = cart.getId();

        //when
        List<CartDetailDto> resultList =
                cartRepository.findCartDetailDtoListByCartId(cartId); //내림차순 출력

        //then
        Assertions.assertEquals(itemList.size()/*5*/, resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println("==========> : " + resultList.get(i));

            Assertions.assertEquals(itemList.get(resultList.size()-1-i).getItemNm(),
                    resultList.get(i).getItemNm());
        }
    }


    @Test
    @DisplayName("카트 아이템 삭제 테스트")
    public void 카트아이템삭제(){
        //given
        int cartSize = 5;

        Member member = saveMember();
        Cart cart = saveCart(member);
        List<CartItem> cartItemList = new ArrayList<>();
        for (int i = 0; i < cartSize; i++) {
            Item tempItem = saveItem(i+"");
            CartItem tempCartItem = saveCartItem(cart, tempItem);

            cartItemList.add(tempCartItem);
        }

        //when
        cartService.deleteCartItem(cartItemList.getFirst().getId());
        cartSize-=1;

        //then
        Cart newCart = cartRepository.findCartByMember(member);
        List<CartItem> newCartItemList = cartItemRepositroy.findByCartId(newCart.getId());
        Assertions.assertEquals(cartSize ,newCartItemList.size());

        for(CartItem cartItem : newCartItemList){
            System.out.println(cartItem);
        }
    }
}
