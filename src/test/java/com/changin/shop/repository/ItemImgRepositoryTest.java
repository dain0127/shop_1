package com.changin.shop.repository;


import com.changin.shop.entity.ItemImg;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
public class ItemImgRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ItemImgRepositoryTest.class);
    @Autowired
    private ItemImgRepository itemImgRepository;

    private ItemImg createItemImg(){
        ItemImg itemImg = new ItemImg();

        itemImg.setImgName("testtesttest");
        itemImg.setOriImgName("test.jpg");
        itemImg.setImgUrl("test/test");
        itemImg.setRepImgYn("Y");

        return itemImg;
    }


    //select test
    @Test
    public void findByIdTest(){
        ItemImg savedItemImg = itemImgRepository.save(createItemImg());
        ItemImg itemImg = itemImgRepository.findById(savedItemImg.getId()).get();
        Assertions.assertEquals(savedItemImg.getId(), itemImg.getId());

//        Optional<ItemImg> imgOptional1
//                = itemImgRepository.findById(352L);
//
//        Assertions.assertTrue(imgOptional1.isPresent());
//        log.info(imgOptional1.toString());
    }
}
