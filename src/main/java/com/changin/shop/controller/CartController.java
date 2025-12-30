package com.changin.shop.controller;

import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.CartItemDto;
import com.changin.shop.dto.CartOrderDto;
import com.changin.shop.dto.PaymentFailResultDto;
import com.changin.shop.entity.Cart;
import com.changin.shop.service.CartService;
import com.changin.shop.service.PortOnePaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final PortOnePaymentService portOneService;


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

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId
            , int count, Principal principal){

        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if(!cartService.vaildateCart(principal.getName(), cartItemId)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity orderItemDelete(@PathVariable Long cartItemId,
                                                        Principal principal){
        if(!cartService.vaildateCart(principal.getName(), cartItemId)){
            return new ResponseEntity<String>("사용자가 일치하지 않습니다."
                    , HttpStatus.FORBIDDEN);
        }
        try {
            cartService.deleteCartItem(cartItemId);
            return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<String>("아이템이 존재하지 않습니다.",
                    HttpStatus.BAD_REQUEST);
        }
    }


    //장바구니 결제 페이지
    @PostMapping(value = "/cart/orderPayment")
    public String orderOrderPayment(Principal principal, Model model
            , @ModelAttribute CartOrderDto cartOrderDto
            , RedirectAttributes redirectAttributes){
        String name = principal.getName();
        Cart cart = cartService.findCartByName(name);
        List<CartDetailDto> content = cartService.findCartDetailByCart(cart);


        //체크된 cartItem 없이 넘어온 경우.
        if(cartOrderDto.getCartOrderDtoList() == null){
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "체크 박스를 하나 이상 선택해야합니다."
            );
            return "redirect:/cart";
        }

        //체크된 아이템만 결제 페이지에서 보이도록 해야한다.
        List<Long> checkedCartItemIdList = cartOrderDto.getCartOrderDtoList()
                .stream().map(CartOrderDto::getCartItemId)
                .toList();


        for (Long checkedCartItemId : checkedCartItemIdList) {
            content = content.stream().filter(e ->{
                return checkedCartItemIdList.contains(e.getCartItemId());
            }).toList();
        }
        content = new ArrayList<>(content);

        model.addAttribute("cartItems", content);

        return "cart/cartOrderPayment";
    }


    //장바구니 주문하기
    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity<?> orderItems(
            @RequestBody CartOrderDto cartOrderDto
            , Principal principal){
        System.out.println("===================>" + cartOrderDto);

        Long cartItemId = cartOrderDto.getCartItemId();
        List<CartOrderDto> cartItemDtoList = cartOrderDto.getCartOrderDtoList();

        if(!cartService.vaildateCart(principal.getName(), cartItemId)){
            return new ResponseEntity<String>("사용자가 일치하지 않습니다."
                    , HttpStatus.FORBIDDEN);
        }

        String paymentId = cartOrderDto.getOrderNumber();

        //to STAY
        Long orderId = cartService.orderCartItemsBeforeVerify(cartOrderDto, principal.getName());
        try {
            //1. db결과랑 지금 결과랑 맞지 않는 것.
            portOneService.verifyPayment(paymentId, orderId);

            //to SUCCESS
            cartService.orderCartItemsAfterVerify(cartOrderDto, orderId);
            return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        }
        catch (IllegalArgumentException | IllegalStateException e){
            portOneService.cancelPayment(paymentId, orderId);
            portOneService.sendPaymentFailDiscordMessage(paymentId, orderId,
                    e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping(value = "/cart/orders/fail")
//    public @ResponseBody ResponseEntity<?> orderFail(
//            @RequestBody PaymentFailResultDto failResultDto){
//
//    }
}
