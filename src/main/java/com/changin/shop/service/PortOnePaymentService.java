package com.changin.shop.service;


import com.changin.shop.constant.OrderStatus;
import com.changin.shop.dto.PaymentFailResultDto;
import com.changin.shop.dto.PortOneDto.PortOnePaymentResponse;
import com.changin.shop.entity.Order;
import com.changin.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PortOnePaymentService {

    final private OrderRepository orderRepository;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    @Value("${portone.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private PortOnePaymentResponse getPayment(String paymentId) {

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
            throw new IllegalStateException("결제 금액 불일치");
        }
    }

    // 1️⃣ 토큰 발급
    private String getAccessToken() {
        String url = "https://api.portone.io/login/api-secret";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
                "apiSecret", apiSecret
        );

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("accessToken");
    }


    // 2️⃣ 결제 취소
    public void cancelPayment(String paymentId, Long orderId) {
        String token = getAccessToken();

        String url = "https://api.portone.io/payments/" + paymentId + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("reason", "db 데이터와 불일치로 인한 결제 취소");
        //body.put("amount", amount); // 전액 취소 시 생략 가능

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (HttpClientErrorException.NotFound e) {
            // 결제 자체가 없음 → 그냥 주문 실패 처리
        } catch (HttpClientErrorException.BadRequest e) {
            // 취소 불가능 상태 → 이미 실패된 결제
        }

        //change OrderStatus to fail
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(OrderStatus.FAIL);

    }

    //결제 실패시 개발자에게 알림
    public void sendPaymentFailDiscordMessage(
            String paymentId,
            Long orderId,
            String reason
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(
                """
                ❌ 결제 실패 발생

                ▷ 주문 ID : %d
                ▷ 결제 ID : %s
                ▷ 실패 사유 : %s

                즉시 확인 바랍니다.
                """
                        .formatted(orderId, paymentId, reason)
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put("content", message.getText()); // Discord는 content 키 사용

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(webhookUrl, request, String.class);
    }

}
