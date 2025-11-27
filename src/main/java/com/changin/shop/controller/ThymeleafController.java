package com.changin.shop.controller;

import com.changin.shop.dto.ItemDto;
import com.changin.shop.dto.ParamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j //log찍어주는 라이브러리
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/ex4")
    public String ex4(Model model) {
        return "thymeleaf/ex4";
    }

    @GetMapping("/ex3")
    public String ex3(ParamDto paramDto, Model model) {

        log.info("==============="+paramDto);
        model.addAttribute("dto", paramDto);

        return "thymeleaf/ex3";
    }


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
