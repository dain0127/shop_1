package com.changin.shop.controller;


import com.changin.shop.dto.ItemDto;
import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.entity.Item;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

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

        //만약 Valid하지 않으면, 제자리로.
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        //이미지 파일을 아무것도 올리지 않았다면 + 등록시라면, 제자리로.
        if(listImgFile.getFirst().isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage"
                    , "하나 이상의 이미지 파일을 등록해야합니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, listImgFile);
        } catch (IOException e) {
            model.addAttribute("errorMessage"
                    , "상품 등록 중 오류가 발생했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
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
