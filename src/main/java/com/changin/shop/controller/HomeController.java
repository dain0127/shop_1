package com.changin.shop.controller;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.service.ItemService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    final int MAX_PAGE_COUNT = 5;
    final int PAGE_SIZE = 6;
    final String mainBannerImgUrl = "/img/banner.jpg";

    @GetMapping("/")
    public String home(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), PAGE_SIZE);

        itemSearchDto.setSearchBy("itemNm");
        itemSearchDto.setSearchSellStatus(ItemSellStatus.SELL);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", MAX_PAGE_COUNT);


        model.addAttribute("mainBannerImgUrl", mainBannerImgUrl);

        return "main";
    }

    @GetMapping("/css-test")
    @ResponseBody
    public String cssTest() {
        checkPath();
        return "css test ok";
    }

    @PostConstruct
    public void checkPath() {
        System.out.println(">>> " + new File(".").getAbsolutePath());
    }
}
