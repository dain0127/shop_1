package com.changin.shop.service;

import com.changin.shop.dto.ItemFormDto;
import com.changin.shop.dto.ItemImgDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImgRepository;
import com.changin.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;


    //세부 사항 가지고 오기
    public ItemFormDto getItemDetail(Long itemId) throws EntityNotFoundException {
        //repository에서 item entity가지고 오기.
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        //해당 item을 참조하는 ItemImg들을 가져오기
        //entity list 에서 dto list로 전환하기.
        List<ItemImgDto> templist =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId)
                        .stream()
                        .map(ItemImgDto::entityToDto).toList();
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(templist);

        //사이즈 5로 맞추기
        for(int i=itemImgDtoList.size();i<5;i++)
            itemImgDtoList.add(new ItemImgDto());

        //dto에 세부사항에 해당하는 정보 저장후 반환.
        ItemFormDto itemFormDto = ItemFormDto.entityToDto(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    //item 정보 업데이트하기
    public Long updateItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws EntityNotFoundException, IOException {
        //1) 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        //변경 감지(dirty checking)
        item.updateItem(itemFormDto);

        //2) 이미지 수정
        List<Long> itemImgIdList = itemFormDto.getItemImgIds();

        for (int i = 0; i < itemImgIdList.size(); i++) {
            itemImgService.updateItemImg(itemImgIdList.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }


    //item 정보 저장하기
    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws IOException {
        //상품 등록
        Item item = itemFormDto.toEntity();
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
