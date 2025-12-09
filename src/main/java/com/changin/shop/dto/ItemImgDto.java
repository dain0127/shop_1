package com.changin.shop.dto;


import com.changin.shop.entity.Item;
import com.changin.shop.entity.ItemImg;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImgDto {
    private Long id;
    private String imgName; //db에 저장될 이미지 이름
    //다운로드 시킬때, 원래 이름으로 다운로드 시키기 위함
    private String oriImgName; //원래 이미지 파일명
    private String imgUrl; //이미지 저장 경로
    private String repImgYn; //대표 이미지 여부


    public static ItemImgDto entityToDto(ItemImg itemImg){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
