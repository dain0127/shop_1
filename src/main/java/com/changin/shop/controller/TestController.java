package com.changin.shop.controller;


import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.dto.TestDto;
import com.changin.shop.service.ItemService;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;

@Slf4j
@Controller
public class TestController {

    @GetMapping(value = "/test")
    public String testGet(){
        return "test/test";
    }

    @PostMapping(value = "/test")
    public String testPost(@ModelAttribute TestDto dto){
        log.info("================>" + dto.toString());
        return "index";
    }

}
