package com.changin.shop.service;


import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.dto.CartOrderDto;
import com.changin.shop.dto.OrderDto;
import com.changin.shop.entity.*;
import com.changin.shop.repository.CartItemRepository;
import com.changin.shop.repository.CartRepository;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    final private CartRepository cartRepository;
    final private CartItemRepository cartItemRepositroy;
    final private ItemRepository itemRepository;
    final private MemberRepository memberRepository;
    final private OrderService orderService;

    //반환값 : cartId
    public Long addCart(CartItemDto cartItemDto, String email){
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        int count = cartItemDto.getCount();

        //member에 카트가 없으면, cart 생성.
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        //cartItem에 item,cart,count 정보 삽입 후 저장.
        //cartItem에 같은 item이 이미 존재하면, 수량 합하기.
        List<CartItem> cartItemList = cartItemRepositroy.findByCartId(cart.getId());
        Boolean isDuplcated = false;
        CartItem newCartItem = null;

        for (CartItem targetCartItem : cartItemList){
            if(targetCartItem.getId().equals(item.getId())){
                newCartItem = targetCartItem;
                isDuplcated = true;
                break;
            }
        }


        if(isDuplcated){
            newCartItem.addCount(count);
        }else{
            newCartItem = CartItem.createCartItem(cart, item, count);
        }

        return cartItemRepositroy.save(newCartItem).getId();
    }

    public Cart findCartByName(String name) {
        Member member = memberRepository.findByEmail(name)
                .orElseThrow(EntityNotFoundException::new);
        return cartRepository.findCartByMember(member);
    }

    public List<CartDetailDto> findCartDetailByCart(Cart cart) {
        return cartRepository.findCartDetailDtoListByCartId(cart.getId());
    }

    public void deleteCartItem(Long cartItemId) throws EntityNotFoundException {
        CartItem cartItem = cartItemRepositroy.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepositroy.delete(cartItem);
    }

    public boolean vaildateCart(String name, Long cartItemId) {
        //cart of member
        Member member = memberRepository.findByEmail(name)
                .orElseThrow(EntityNotFoundException::new);
        Cart memberCart = cartRepository.findCartByMember(member);

        //cart of cartItem
        CartItem cartItem = cartItemRepositroy.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Cart cartItemCart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(EntityNotFoundException::new);

        //return value by whether same or not
        return memberCart.getId().equals(cartItemCart.getId()) ? true : false;
    }

    //선택한 여러개의 상품을 주문한다.
    public Long orderCartItems(CartOrderDto cartOrderDto, String name) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        Long orderId = -1L;

        //order cartItemWisely
        for(CartOrderDto dto : cartOrderDtoList){
            CartItem cartItem = cartItemRepositroy.findById(dto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            Long itemId = itemRepository.findById(cartItem.getItem().getId())
                            .orElseThrow(EntityNotFoundException::new).getId();

            int count = cartItem.getCount();
            orderId = orderService.order(new OrderDto(itemId, (long) count), name);

            //delete cartItemList from cart
            cartItemRepositroy.delete(cartItem);
        }

        return cartOrderDtoList.isEmpty() ? -1L : orderId;
    }
}
