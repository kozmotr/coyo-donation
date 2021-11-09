package com.coyoapp.donations.payment;

import com.coyoapp.donations.paypal.PaypalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/api/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaypalService paypalService;
    private final PaymentService paymentService;

    @GetMapping("token")
    public ResponseEntity generateToken()  {
        return ResponseEntity.ok(TokenResponse.builder()
                .token(paypalService.generateClientToken())
                .build());
    }

    @PostMapping("checkout")
    public ResponseEntity checkout(@RequestBody PaymentRequest request) {
        PaypalService.PaypalResult result = paymentService.processPayment(request);
        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body("Transaction couldn't be completed.");
    }

}
