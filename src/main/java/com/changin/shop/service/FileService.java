package com.changin.shop.service;


import com.changin.shop.entity.ItemImg;
import com.changin.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
//파일을 저장하는 서비스
public class FileService {

    //파일 업로드. (파일 시스템에.)
    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws IOException {

        //파일 이름 만들기
        UUID uuid = UUID.randomUUID(); //UUID (Universally Unique ID). 즉, 보편적으로 유일한 ID 생성.
        String extension = originalFileName
                .substring(originalFileName.lastIndexOf(".")); //확장자명 추출
        String saveFileName = uuid.toString() + extension;

        //업로드 경로 지정.
        String fileUploadFullName = uploadPath + "/" + saveFileName;

        //파일 쓰기
        try{
            FileOutputStream fout = new FileOutputStream(fileUploadFullName);
            fout.write(fileData);
            fout.close();
        }
        catch (IOException e){
            log.info("======================" + e.getMessage());
        }


        return saveFileName;
    }

    //파일 삭제.
    public void deleteFile(String filePath){
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("=======================" + "파일이 존재합니다. 삭제되었습니다.");
        }else {
            log.info("=======================" + "파일이 존재하지 않습니다.");
        }
    }
}
