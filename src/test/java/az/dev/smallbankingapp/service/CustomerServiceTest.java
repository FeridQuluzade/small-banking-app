package az.dev.smallbankingapp.service;

import static az.dev.smallbankingapp.entity.UserType.NON_VERIFIED;
import static az.dev.smallbankingapp.properties.TestConstants.GSM_NUMBER;
import static az.dev.smallbankingapp.properties.TestConstants.OTP_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.dto.request.VerifyCustomerRequest;
import az.dev.smallbankingapp.entity.CustomerAccount;
import az.dev.smallbankingapp.entity.User;
import az.dev.smallbankingapp.repository.CustomerAccountsRepository;
import az.dev.smallbankingapp.security.CustomAuthentication;
import az.dev.smallbankingapp.security.UserPrincipal;
import java.math.BigDecimal;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private OtpService otpService;

    @Mock
    private UserService userService;

    @Mock
    private CustomerAccountsRepository customerAccountsRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        var userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(GSM_NUMBER);
        Authentication customAuthentication =
                new CustomAuthentication(userPrincipal, new HashSet<>());
        SecurityContextHolder.getContext().setAuthentication(customAuthentication);
    }

    @Test
    void verify_Success() {
        //given
        var verifyCustomerRequest = new VerifyCustomerRequest();
        verifyCustomerRequest.setCode(OTP_CODE);

        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);
        otpRequest.setOtpCode(OTP_CODE);

        var user = new User();
        user.setGsmNumber(GSM_NUMBER);
        user.setUserType(NON_VERIFIED);

        var customerAccount = new CustomerAccount();
        customerAccount.setUser(user);
        customerAccount.setBalance(BigDecimal.valueOf(100));

        //when
        doNothing().when(otpService).verify(otpRequest);
        when(userService.findByGsmNumber(GSM_NUMBER)).thenReturn(user);
        when(customerAccountsRepository.save(customerAccount)).thenReturn(customerAccount);

        //then
        var actual = customerService.verify(verifyCustomerRequest);

        assertEquals(BigDecimal.valueOf(100), actual.getInitialBalance());

        verify(otpService, times(1)).verify(otpRequest);
        verify(userService, times(1)).findByGsmNumber(GSM_NUMBER);
        verify(customerAccountsRepository, times(1)).save(customerAccount);
    }

}
