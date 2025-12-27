package com.changin.shop.entity;


import com.changin.shop.common.entity.BaseEntity;
import com.changin.shop.constant.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false, unique = true)
    private String categoryNm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryStatus active;

    public void activate() {
        this.active = CategoryStatus.ACTIVE;
    }

    public void deactivate() {
        this.active = CategoryStatus.IDEL;
    }

    public void updateName(String name) {
        this.categoryNm = name;
    }
}
