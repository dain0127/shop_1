package com.changin.shop.service;


import com.changin.shop.constant.OrderStatus;
import com.changin.shop.dto.PortOneDto.PortOnePaymentResponse;
import com.changin.shop.entity.Order;
import com.changin.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PortOnePaymentService {

    final private OrderRepository orderRepository;


    @Value("${portone.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public PortOnePaymentResponse getPayment(String paymentId) {

        String url = "https://api.portone.io/payments/" + paymentId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + apiSecret);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PortOnePaymentResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        PortOnePaymentResponse.class
                );

        return response.getBody();
    }

    @Transactional
    public void verifyPayment(String paymentId, Long orderId) {

        // 1. 포트원 결제 조회
        PortOnePaymentResponse payment = this.getPayment(paymentId);

        // 2. 우리 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        int orderTotalPrice = order.getTotalPrice();
        int paidAmount = payment.getAmount().getTotal();

        // 3. 상태 검증
        if (!"PAID".equals(payment.getStatus())) {
            throw new IllegalStateException("결제가 완료되지 않았습니다.");
        }

        // 4. 금액 검증 (🔥 핵심)
        if (orderTotalPrice != paidAmount) {
            /*
                ★★★★★★★★★★★★★★★★★★★★★★★1. 결제 실패시 DB에 로그 남기기★★★★★★★★★★★★★★★★★★★★★★★★★

                ★★★★★★★★★★★★★★★★★★★★★★★2. 관리자에게 알람 날아가도록 하기. 메일이나 카톡으로 날리기. 혹은 문자.★★★★★★★★★★★★★★★★★★★★★★★★★

             */
            throw new IllegalStateException("결제 금액 불일치");
        }

        // 5. 주문 확정
        order.setOrderStatus(OrderStatus.SUCCESS);
    }
}
