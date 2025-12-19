package com.changin.shop.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    @NotNull(message = "상품 아이디 입력값은 필수입니다.")
    private Long itemId;

    @NotNull
    @Min(value = 1, message = "상품은 최소 1개 이상 주문해야합니다.")
    private int count;
}
