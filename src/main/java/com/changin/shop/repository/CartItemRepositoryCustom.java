package com.changin.shop.repository;

import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.ItemAdminDto;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemRepositoryCustom {

    public List<CartDetailDto> findCartDetailDtoList(Long cartId, String email);
}
