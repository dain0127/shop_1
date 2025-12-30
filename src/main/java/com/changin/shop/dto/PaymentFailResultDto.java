package com.changin.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentFailResultDto {
    private String paymentId;
    private String status;
    private String failMessage;
}