package com.changin.shop.controller;


import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.entity.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {


    //상품 등록 페이지 전송
    @GetMapping("/admin/item/new")
    public String itemNewForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    //상품 등록
    @PostMapping("/admin/item/new")
    public String itemNew(@ModelAttribute ItemFormDto itemFormDto) {
        return "";
    }



    //상품 관리 페이지 전송
    @GetMapping("/admin/items")
    public String itemManagementForm(){
        return "item/itemList";
    }


    //상품 관리
    @PostMapping("/admin/items")
    public String itemManagement(){
        return "";
    }

}
