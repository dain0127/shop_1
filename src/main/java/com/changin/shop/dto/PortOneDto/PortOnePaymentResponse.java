package com.changin.shop.dto.PortOneDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PortOnePaymentResponse {

    private String id;                 // paymentId
    private String status;             // PAID, FAILED, CANCELED
    private Amount amount;
    private String orderName;

    @Getter
    public static class Amount {
        private int total;
    }
}