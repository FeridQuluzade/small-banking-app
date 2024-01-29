package az.dev.smallbankingapp.controller;

import az.dev.smallbankingapp.dto.request.PaymentRequest;
import az.dev.smallbankingapp.dto.response.PaymentResponse;
import az.dev.smallbankingapp.service.PaymentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public PaymentResponse process(@Valid @RequestBody PaymentRequest paymentRequest) {
        return paymentService.process(paymentRequest);
    }

}
