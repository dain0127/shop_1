package com.changin.shop.controller;


import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Value("${uploadPath}")
    String uploadPath;

    final int MAX_PAGE_COUNT = 5;
    final int PAGE_SIZE = 6;

    @GetMapping("/")
    public String home(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), PAGE_SIZE);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", MAX_PAGE_COUNT);


        model.addAttribute("mainBannerImgLocation", uploadPath
                + "/image_sample/banner/banner.jpg");

        return "main";
    }
}
