package com.changin.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class TestCode {

    @Test
    public void 놀이터(){
        //given
        List<String> allContent = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            allContent.add(i+"");
        }


        //when
        Pageable pageable = PageRequest.of(0, 5);
        Page<String> page;

        List<String> content;

        //then
        System.out.println("===================>" + pageable);
        for (int i = 0; i < 4; i++) {
            pageable = PageRequest.of(i,5);
            content = new ArrayList<>();
            for (int j = 0; j < pageable.getPageSize(); j++) {
                content.add(allContent.get(i*5+j));
            }

            page = new PageImpl<>(content, pageable, content.size());

            System.out.println(page.getContent());
        }
    }
}
