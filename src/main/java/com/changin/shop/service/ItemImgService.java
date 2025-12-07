package com.changin.shop.service;


import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemImgService {

    @Value("${itemImgLocation}")
    String uploadPath;

    private final FileService fileService;
    private final ItemImageRepository imgRepo;

    //이미지 저장 서비스
    //이미지 데이터와, 이름, 저장경로대로. 파일 시스템에 파일을 저장, DB에 url 저장.
    public void saveImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        //파일을 저장.
        String originalFileName = itemImg.getOriImgName();
        String imgName;
        String imgUrl;

        //이미지 파일을 로컬 환경에 저장
        if(!StringUtils.isEmpty(originalFileName)) {
            imgName = fileService.uploadFile(uploadPath
                    , originalFileName, itemImgFile.getBytes());
            imgUrl = "/items/image/" + imgName;

            //경로 DB에 저장하기.
            itemImg.updateItemImg(originalFileName
                    , imgName, imgUrl);
            imgRepo.save(itemImg);
        }

    }

    //파일 삭제.
    public void deleteImg(String filePath){
        fileService.deleteFile(filePath);

        //DB에서 해당 파일 경로 삭제.
        ItemImg itemImg = imgRepo.findByFilePath(filePath)
                .orElseThrow(NoSuchElementException::new);
        imgRepo.delete(itemImg);
    }
}
