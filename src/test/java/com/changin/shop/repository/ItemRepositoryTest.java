package com.changin.shop.repository;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;

    public void createItemList() {
        for(int i=1;i<=10;i++){
            Item item = Item.builder()
                    .itemNm("0"+i)
                    .price(10000+i)
                    .stockNumber(1)
                    .itemDetail("hello"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            itemRepo.save(item);
        }
    }

    @Test
    @DisplayName("JPQL native 테스트")
    public void findByNativeDetailTest(){
        createItemList();
        itemRepo.findByNativeDetail("1")
                .forEach((item) -> {System.out.println(item);});
    }


    @Test
    @DisplayName("JPQL 테스트")
    public void findByDetailTest(){
        createItemList();
        itemRepo.findByDetail("1")
                .forEach((item) -> {System.out.println(item);});
    }



    @Test
    public void findByItemNmLessThanOrderByItemNmDescTest() {
        createItemList();
        itemRepo.findByItemNmLessThanOrderByItemNmDesc("05")
                .forEach(System.out::println);

    }


    @Test
    public void findByItemNmLessThanTest(){
        createItemList();
        itemRepo.findByItemNmLessThan("03")
                .forEach(System.out::println);
    }

    @Test
    public void findByItemNmOrItemDetailTest() {
        createItemList();
        itemRepo.findByItemNmOrItemDetail("aaa","hello5")
                .forEach(System.out::println);
    }

    @Test
    public void findByItemNmTest(){
        createItemList();
        itemRepo.findByItemNm("05")
                .forEach(System.out::println);

        
    }

    //DB에 아이템 저장 테스트
    @Test
    @DisplayName("상품 생성 테스트")
    public void createItemTest(){
        Item item = Item.builder()
                .itemNm("ddd")
                .price(100)
                .stockNumber(1)
                .itemDetail("dddd")
                .itemSellStatus(ItemSellStatus.SELL)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        System.out.println("=============== item : " + item);
        Item savedItem = itemRepo.save(item);
        System.out.println("=============== item : " + savedItem);


    }

}
