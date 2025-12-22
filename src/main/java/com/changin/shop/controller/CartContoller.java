package com.changin.shop.controller;

import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartContoller {

    private final CartService cartService;


    //카트 추가하기
    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order
            (@RequestBody @Valid CartItemDto cartItemDto,
             BindingResult bindingResult, Principal principal){

        //Dto 데이터의 무결성 검증및 처리
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //카트 리스트
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        String name = principal.getName();
        Cart cart = cartService.findCartByName(name);
        List<CartDetailDto> content = cartService.findCartDetailByCart(cart);
        model.addAttribute("cartItems", content);

        return "cart/cartList";
    }
}
