package com.coyoapp.donations.paypal;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.coyoapp.donations.payment.PayPalPaymentRequest;
import com.coyoapp.donations.payment.PaypalNonceResult;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Mocked out PayPal payment provider.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaypalService {

    private final RestTemplate restTemplate;

    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    @SneakyThrows
    public String generateClientToken() {
        Future<String> getOperation = executor.submit(()->{
            PaypalNonceResult request = restTemplate.getForObject("http://PAYPAL-SERVICE/paypal/nonce", PaypalNonceResult.class);
            return request.getNonce();
        });
        while(!getOperation.isDone()){
            Thread.sleep(100);
        }
        return getOperation.get();

    }

    @SneakyThrows
    public PaypalResult processCheckout(String paymentMethodNonce, BigDecimal amount) {
        Future<PaypalService.PaypalResult> resultFuture = executor.submit(()->{
            PayPalPaymentRequest payPalPaymentRequest = PayPalPaymentRequest.of(paymentMethodNonce, amount);
            PaypalService.PaypalResult result = restTemplate.postForObject("http://PAYPAL-SERVICE/paypal/checkout",payPalPaymentRequest, PaypalService.PaypalResult.class);
            return result;
        });
        while(!resultFuture.isDone()){
            Thread.sleep(100);
        }
        log.trace("processCheckout(paymentMethodNonce={}, amount={})", paymentMethodNonce, amount);
        return resultFuture.get();
    }



    @Data
    @NoArgsConstructor
    public static class PaypalResult {
        String transactionId;
        boolean success;
    }
}
