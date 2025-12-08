package com.changin.shop.service;

import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.dto.ItemImgDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImageRepository;
import com.changin.shop.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImageRepository itemImageRepository;

    public Long saveItem(ItemFormDto itemFormDtom,
                         List<MultipartFile> itemImgFileList) throws IOException {
        //상품 등록
        Item item = itemFormDtom.toEntity();
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }
}
