package com.changin.shop.controller;


import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.entity.Item;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    //상품 등록 페이지 전송
    @GetMapping("/admin/item/new")
    public String itemNewForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    //상품 등록
    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto,
                          BindingResult bindingResult,
      @RequestParam("itemImgFile") List<MultipartFile> listImgFile,
                          Model model) {

        //만약 Valid하지 않으면,
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        itemService.saveItem(itemFormDto.toEntity());

        if(listImgFile.isEmpty()) {
            model.addAttribute("errorMessage"
                    , "하나 이상의 사진을 입력해야합니다.");

            
        }

        return "item/itemForm";
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
