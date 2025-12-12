package com.changin.shop.controller;


import com.changin.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String home(Model model){
        Long latestItemId = itemService.findLatestItemId();
        model.addAttribute("latestItemId", latestItemId);
        return "index";
    }
}
