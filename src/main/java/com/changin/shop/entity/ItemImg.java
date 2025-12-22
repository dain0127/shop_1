package com.changin.shop.entity;


import com.changin.shop.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_img_id")
    private Long id;

    private String imgName; //db에 저장될 이미지 이름
    //다운로드 시킬때, 원래 이름으로 다운로드 시키기 위함
    private String oriImgName; //원래 이미지 파일명
    private String imgUrl; //이미지 저장 경로
    private String repImgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
