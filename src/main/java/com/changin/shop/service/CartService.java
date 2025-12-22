package com.changin.shop.service;


import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.entity.CartItem;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.Member;
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
}
