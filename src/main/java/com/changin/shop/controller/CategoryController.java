package com.changin.shop.controller;


import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.dto.CategoryDto;
import com.changin.shop.service.CategoryService;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/admin/category/new")
    public String newCategoryForm(Model model){
        List<CategoryDto> categoryDtoList =
            categoryService.getCategories();
        model.addAttribute("categories", categoryDtoList);
        return "/category/category";
    }

    @PostMapping(value = "/admin/category/new")
    public String newCategoryForm(@ModelAttribute CategoryDto categoryDto
            , BindingResult bindingResult){
        categoryService.saveCategory(categoryDto);
        return "redirect:/admin/category/new";
    }

    @PatchMapping(value = "/admin/category/update/{id}")
    public @ResponseBody ResponseEntity categoryUpdate
            (@PathVariable("id") Long categoryId, Principal principal,
             @RequestParam("newCategoryName") String newCategoryName){
        try {
            categoryService.updateCategory(categoryId, newCategoryName);
            return new ResponseEntity<Long>(categoryId, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<String>("아이템이 존재하지 않습니다.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/admin/category/delete/{id}")
    public @ResponseBody ResponseEntity categoryDelete(@PathVariable("id") Long categoryId,
                                                        Principal principal){

        try {
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<Long>(categoryId, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<String>("아이템이 존재하지 않습니다.",
                    HttpStatus.BAD_REQUEST);
        }
    }

}
