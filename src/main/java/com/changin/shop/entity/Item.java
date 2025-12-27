package com.changin.shop.entity;


import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.common.entity.BaseTimeEntity;
import com.changin.shop.constant.ItemSellStatus;

import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm; //상품 이름
    private int price; //가격
    private int stockNumber; //재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //아이템 판매 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void updateItem(ItemFormDto dto, Category category){
        this.itemNm = dto.getItemNm();
        this.price = dto.getPrice();
        this.stockNumber = dto.getStockNumber();
        this.itemDetail = dto.getItemDetail();
        this.itemSellStatus = dto.getItemSellStatus();
        this.category = category;
    }

    public void removeStock(int stockNumber){
        if(this.stockNumber - stockNumber < 0)
            throw new OutOfStockException("남아있는 재고가 부족합니다. (남은 재고) : " + this.stockNumber);
        else
            this.stockNumber -= stockNumber;
    }

    public void addStock(int stockNumber){
            this.stockNumber += stockNumber;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }
}
