package com.changin.shop.repository;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@SpringBootTest
@Transactional
public class ItemRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ItemRepositoryTest.class);
    @Autowired
    ItemRepository itemRepo;

    @PersistenceContext
    private EntityManager em;


    public void saveItemList() {
        for(int i=1;i<=10;i++){

            //번갈아가면서 판매 상태 세팅
            ItemSellStatus st = i<=5 ? ItemSellStatus.SELL : ItemSellStatus.SOLD_OUT;

            Item item = Item.builder()
                    .itemNm("0"+i)
                    .price(10000+i)
                    .stockNumber(1)
                    .itemDetail("hello"+i)
                    .itemSellStatus(st)
                    .build();

            itemRepo.save(item);
        }
    }

    @Test
    @DisplayName("item 생성/수정 시간 테스트")
    @WithMockUser(username = "testUserName", roles = "USER")
    public void createAndModifyItem(){
        //given
        Item item = Item.builder()
                .itemNm("0")
                .price(10000)
                .stockNumber(1)
                .itemDetail("hello")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();


        //when
        itemRepo.save(item);

        em.flush();
        em.clear();

        //then
        Item savedItem = itemRepo.findByItemNm("0").getFirst();
        log.info(savedItem.toString());
    }




    @Test
    @DisplayName("querydsl predicate executor 테스트")
    public void querydslTest2(){
        saveItemList();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemSellStatus = null;

        String keyName = "hello";
        int keyPrice = 10000;

        booleanBuilder.and(item.itemDetail.like("%" + keyName + "%"));
        booleanBuilder.and(item.price.gt(keyPrice));

        //이건 왜 if문을 써야하는가.
        if(StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }


        Pageable pageable = PageRequest.of(0,10);
        Page<Item> page = itemRepo.findAll(booleanBuilder, pageable);

        List<Item> list = page.getContent();
        list.stream().forEach(System.out::println);
    }


    @Test
    @DisplayName("querydsl 테스트")
    public void querydslTest(){
        saveItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Tuple> result =
                queryFactory
                        .select(QItem.item.price, QItem.item.count())
                        .from(QItem.item)
                        .where(QItem.item.itemSellStatus.eq(ItemSellStatus.SELL))
                        .where(QItem.item.itemDetail.like("%1%"))
                        .groupBy(QItem.item.price);


        result.fetch().forEach(System.out::println);
    }



    @Test
    @DisplayName("JPQL native 테스트")
    public void findByNativeDetailTest(){
        saveItemList();
        itemRepo.findByNativeDetail("1")
                .forEach((item) -> {System.out.println(item);});
    }


    @Test
    @DisplayName("JPQL 테스트")
    public void findByDetailTest(){
        saveItemList();
        itemRepo.findByDetail("1")
                .forEach((item) -> {System.out.println(item);});
    }



    @Test
    public void findByItemNmLessThanOrderByItemNmDescTest() {
        saveItemList();
        itemRepo.findByItemNmLessThanOrderByItemNmDesc("05")
                .forEach(System.out::println);

    }


    @Test
    public void findByItemNmLessThanTest(){
        saveItemList();
        itemRepo.findByItemNmLessThan("03")
                .forEach(System.out::println);
    }

    @Test
    public void findByItemNmOrItemDetailTest() {
        saveItemList();
        itemRepo.findByItemNmOrItemDetail("aaa","hello5")
                .forEach(System.out::println);
    }

    @Test
    public void findByItemNmTest(){
        saveItemList();
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
                .build();

        System.out.println("=============== item : " + item);
        Item savedItem = itemRepo.save(item);
        System.out.println("=============== item : " + savedItem);


    }

}
