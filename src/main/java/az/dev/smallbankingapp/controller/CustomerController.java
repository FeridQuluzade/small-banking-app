package az.dev.smallbankingapp.controller;

import az.dev.smallbankingapp.dto.request.VerifyCustomerRequest;
import az.dev.smallbankingapp.dto.response.VerifyCustomerResponse;
import az.dev.smallbankingapp.service.CustomerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/verify")
    public VerifyCustomerResponse verify(@Valid @RequestBody VerifyCustomerRequest request) {
        return customerService.verify(request);
    }

}
