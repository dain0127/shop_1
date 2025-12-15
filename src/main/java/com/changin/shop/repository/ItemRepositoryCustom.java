package com.changin.shop.repository;

import com.changin.shop.dto.ItemAdminDto;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<ItemAdminDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
