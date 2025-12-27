package com.changin.shop.dto;
import com.changin.shop.constant.CategoryStatus;
import com.changin.shop.entity.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long categoryId;

    @NotNull
    private String categoryNm;

    public Category toEntity() {
        return Category.builder()
                .categoryNm(this.categoryNm)
                .active(CategoryStatus.ACTIVE)
                .build();
    }
}
