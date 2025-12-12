package com.changin.shop.service;


import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    String itemImgLocation;

    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;

    // WebMvcConfig 클래스를 참조하라.
    // /images/**는 url로 설정되어있고,
    // /item_img_save/ 는 로컬 파일 시슽메의 경로로서 설정되어있음에 주목하라.
    private final String ItemimgUrl = "/images/item_img_save/";
    
    
    //이미지 수정 서비스
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws IOException{

        if(!itemImgFile.isEmpty()){
            String imgName;
            String originalFileName = itemImgFile.getOriginalFilename();
            String imgUrl;

            //이미지 파일을 로컬 환경에서 수정(삭제 후 등록)
            if(!StringUtils.isEmpty(originalFileName)) {
                //로컬 파일 삭제 후 등록
                ItemImg itemImg = itemImgRepository.findById(itemImgId)
                        .orElseThrow(EntityNotFoundException::new);
                if(!StringUtils.isEmpty(itemImg.getImgName()))
                    fileService.deleteFile(itemImgLocation + "/"
                            + itemImg.getImgName());


                imgName = fileService.uploadFile(itemImgLocation
                        , originalFileName, itemImgFile.getBytes());


                //DB에 있는 데이터 수정
                //dirty checking (jpa. DB 자동 수정)
                imgUrl = ItemimgUrl + imgName;
                itemImg.updateItemImg(originalFileName,imgName,imgUrl);
            }
        }
    }
    
    //이미지 저장 서비스
    //이미지 데이터와, 이름, 저장경로대로. 파일 시스템에 파일을 저장, DB에 url 저장.
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException{
        String originalFileName = itemImgFile.getOriginalFilename();
        String imgName;
        String imgUrl;

        if(!StringUtils.isEmpty(originalFileName)) {
            //이미지 파일을 로컬 환경에 저장
            imgName = fileService.uploadFile(itemImgLocation
                    , originalFileName, itemImgFile.getBytes());
            imgUrl = ItemimgUrl + imgName;

            //경로 DB에 저장하기.
            itemImg.updateItemImg(originalFileName
                    , imgName, imgUrl);
            itemImgRepository.save(itemImg);
        }

    }

    //파일 삭제.
    public void deleteItemImg(String fileName){
        fileService.deleteFile(itemImgLocation + "/" + fileName);

        //DB에서 해당 파일 경로 삭제.
        ItemImg itemImg = itemImgRepository.findByImgUrl(ItemimgUrl + fileName)
                .orElseThrow(NoSuchElementException::new);
        itemImgRepository.delete(itemImg);
    }
}
