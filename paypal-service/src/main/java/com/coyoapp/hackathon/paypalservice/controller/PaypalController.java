package com.coyoapp.hackathon.paypalservice.controller;


import com.coyoapp.hackathon.paypalservice.data.dto.NonceResult;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentRequest;
import com.coyoapp.hackathon.paypalservice.data.dto.PaymentResult;
import com.coyoapp.hackathon.paypalservice.service.PaypalService;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/paypal/")
@RestController
public class PaypalController {


    @Autowired
    private PaypalService paypalService;

    @PostMapping(value = "checkout")
    public ResponseEntity processCheckout(@RequestBody PaymentRequest request) {
        PaymentResult result = paypalService.processPayment(request);

        return ResponseEntity.ok().body(result);
    }


    @GetMapping(value = "nonce")
    public ResponseEntity<NonceResult> createNonce(){
        return ResponseEntity.ok(paypalService.createNonce());
    }
}
