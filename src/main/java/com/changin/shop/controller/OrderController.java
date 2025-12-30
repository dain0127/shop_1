package com.changin.shop.controller;


import com.changin.shop.dto.OrderDto;
import com.changin.shop.dto.OrderHistDto;
import com.changin.shop.service.OrderService;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    //주문하기
    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                      BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors){
                sb.append(fieldErrors.getLast().getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        log.info("==========================> " + email);

        try{
            orderId = orderService.order(orderDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }



    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }


    //사용자가 "/order/{orderId}/cancel"에 post 방식으로 request를 전송할 때,
    //1. 해당 사용자의 정말로 해당 페이지의 orderId의 주인과 같은지 검증한다
    //2. order를 삭제한다.
    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity orderCancel(@PathVariable("orderId") Long orderId, Principal principal){

        if(!orderService.vaildateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("사용자가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/verification")
    public ResponseEntity<?> orderVerification(@PathVariable("orderId") Long orderId, Principal principal){

        if(!orderService.vaildateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("사용자가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping("/order/complete")
    public String orderCompleteForm(){
        return "order/orderComplete";
    }
}
