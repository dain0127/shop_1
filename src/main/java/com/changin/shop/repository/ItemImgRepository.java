package com.changin.shop.repository;


import com.changin.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    Optional<ItemImg> findByImgUrl(String imgUrl);
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
