package com.changin.shop.controller;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.ItemAdminDto;
import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImgRepository;
import com.changin.shop.repository.ItemRepository;
import com.changin.shop.service.CategoryService;
import com.changin.shop.service.ItemImgService;
import com.changin.shop.service.ItemService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.changin.shop.constant.MainPageConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private final CategoryService categoryService;


    //상품 등록 페이지 전송
    @GetMapping("/admin/item/new")
    public String itemNewForm(Model model) {
        //dummy를 보내, html 페이지에서 해당 dto의 field에 접근 할 수 있도록 한다.
        model.addAttribute("itemFormDto", new ItemFormDto());
        model.addAttribute("categories"
                , categoryService.getCategories());
        return "item/itemForm";
    }


    //상품 등록
    @PostMapping("/admin/item/new")
    public String itemNew(@Valid @ModelAttribute ItemFormDto itemFormDto,
                          BindingResult bindingResult,
                          @RequestParam("itemImgFile") List<MultipartFile> listImgFile,
                          Model model) {

        //만약 Valid하지 않으면, 제자리로.
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories());
            return "item/itemForm";
        }

        //이미지 파일을 아무것도 올리지 않았다면 + 등록시라면(hidden으로 넘어온 id값이 없음.), 제자리로.
        if(listImgFile.getFirst().isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage"
                    , "하나 이상의 이미지 파일을 등록해야합니다.");
            model.addAttribute("categories", categoryService.getCategories());
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


    //아이템 수정 폼 (세부사항 자동 전송)
    @GetMapping("/admin/item/{item_id}") // /admin/item/1
    public String itemUpdate(Model model
            , @PathVariable("item_id") Long itemId){
        try{
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            model.addAttribute("categories"
                    , categoryService.getCategories());
            return "item/itemForm";
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            model.addAttribute("errorMessage", "등록된 상품이 없습니다.");
            return "item/itemForm";
        }
    }


    //아이템 수정
    @PostMapping("/admin/item/{item_id}")
    public String itemUpdate(Model model
            , @PathVariable("item_id") Long itemId
            , @Valid @ModelAttribute ItemFormDto itemFormDto
            , BindingResult bindingResult
            , @RequestParam("itemImgFile") List<MultipartFile> listImgFile){

        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.getCategories());
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, listImgFile);
        } catch (IOException e) {
            model.addAttribute("errorMessage"
                    , "상품 수정 중 오류가 발생했습니다.");
            return "item/itemForm";
        }

        return  "redirect:/";
    }


    //상세 페이지
    @GetMapping("/item/{item_id}")
    public String itemDetail(Model model
            ,@PathVariable("item_id") Long itemId){
        try{
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("item", itemFormDto);
            return "item/itemDetail";
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            model.addAttribute("errorMessage", "등록된 상품이 없습니다.");
            return "/";
        }
    }

    //카테고리 아이템 리스트
    @GetMapping("/items/category/{id}")
    public String categoryItemList(ItemSearchDto itemSearchDto, Optional<Integer> page,
            Model model, @PathVariable("id")Long categoryId){
        Pageable pageable = PageRequest.of(page.orElse(0), PAGE_SIZE);

        itemSearchDto.setSearchBy("itemNm");
        itemSearchDto.setSearchSellStatus(ItemSellStatus.SELL);
        itemSearchDto.setCategoryId(categoryId);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", MAX_PAGE_COUNT);


        model.addAttribute("mainBannerImgUrl", null);

        return "main";
    }


    //상품 리스트 페이지 전송
    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String itemManagementForm(ItemSearchDto itemSearchDto, Model model, @PathVariable(value = "page") Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get(): 0, 5);
        Page<ItemAdminDto> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        log.info(items.getContent().getFirst().getCategoryNm());

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);


        return "item/itemList";
    }
}
