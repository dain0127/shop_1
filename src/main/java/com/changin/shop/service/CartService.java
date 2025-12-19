package com.changin.shop.service;


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
        Cart cart;
        if((cart = cartRepository.findByMemberId(member.getId())) == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        //cartItem에 item,cart,count 정보 삽입 후 저장.
        //cartItem이 이미 존재하면, 수량 합하기.
        CartItem cartItem = cartItemRepositroy.findByCartId(cart.getId());
        if(cartItem == null){
            cartItem = CartItem.createCartItem(cart, item, count);
        }else{
            cartItem.addCount(count);
        }

        return cartItemRepositroy.save(cartItem).getId();
    }

}
