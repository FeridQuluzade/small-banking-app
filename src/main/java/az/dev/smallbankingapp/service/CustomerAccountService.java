package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.dto.request.VerifyCustomerRequest;
import az.dev.smallbankingapp.dto.response.VerifyCustomerResponse;
import az.dev.smallbankingapp.entity.AccountBalanceProjection;
import az.dev.smallbankingapp.entity.CustomerAccount;
import az.dev.smallbankingapp.entity.User;
import az.dev.smallbankingapp.entity.UserType;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.repository.CustomerAccountsRepository;
import az.dev.smallbankingapp.util.RequestContextUtil;
import az.dev.smallbankingapp.util.UuidProvider;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAccountService {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100);

    private final OtpService otpService;
    private final UserService userService;
    private final CustomerAccountsRepository customerAccountsRepository;

    public VerifyCustomerResponse verify(VerifyCustomerRequest request) {
        String gsmNumber = RequestContextUtil.getUsername();

        otpService.verify(
                OtpRequest.withOtpCode(gsmNumber, request.getCode()));

        User user = userService.findByGsmNumber(gsmNumber);
        userService.updateUserStatus(user, UserType.VERIFIED);

        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setBalance(INITIAL_BALANCE);
        customerAccount.setUser(user);
        customerAccount.setAccountNumber(UuidProvider.generate());
        customerAccountsRepository.save(customerAccount);

        return new VerifyCustomerResponse(INITIAL_BALANCE);
    }

    protected AccountBalanceProjection findProjectionByGsmNumber(String gsmNumber) {
        return customerAccountsRepository.findByGsmNumber(gsmNumber)
                .orElseThrow(() -> ServiceException.of(ErrorCodes.CUSTOMER_ACCOUNT_NOT_FOUND));
    }

    protected void updateBalanceByAccountNumber(String accountNumber, BigDecimal balance) {
        customerAccountsRepository.updateBalanceByAccountNumber(accountNumber, balance);
    }

}
