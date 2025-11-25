package com.changin.shop.repository;

import com.changin.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    List<Item> findByItemNmLessThan(String itemNm);
    List<Item> findByItemNmLessThanOrderByItemNmDesc(String itemNm);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByNativeDetail(@Param("itemDetail") String itemDetail);
}
