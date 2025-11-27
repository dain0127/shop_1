package com.changin.shop.controller;

import com.changin.shop.dto.ItemDto;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/ex2")
    public String ex2(Model model){


        return "thymeleaf/ex2";
    }

    @GetMapping("/ex1")
    public String ex1(Model model) {
        ItemDto itemDto = ItemDto.builder()
                .itemNm("최신 스프링")
                .itemDetail("스프링 부트 3.1.4")
                .itemSellStatus("SELL")
                .price(20000)
                .build();


        model.addAttribute("itemDto",  itemDto);

        return "thymeleaf/ex1";
    }
}
