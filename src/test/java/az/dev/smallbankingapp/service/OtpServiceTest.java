package az.dev.smallbankingapp.service;

import static az.dev.smallbankingapp.properties.TestConstants.ATTEMPTS;
import static az.dev.smallbankingapp.properties.TestConstants.FIRST_TIME_SENDING;
import static az.dev.smallbankingapp.properties.TestConstants.GSM_NUMBER;
import static az.dev.smallbankingapp.properties.TestConstants.ID;
import static az.dev.smallbankingapp.properties.TestConstants.NUMBER_OF_SENDING;
import static az.dev.smallbankingapp.properties.TestConstants.OTP_CODE;
import static az.dev.smallbankingapp.properties.TestConstants.OTP_LENGTH;
import static az.dev.smallbankingapp.properties.TestConstants.SENT_TIME;
import static az.dev.smallbankingapp.properties.TestConstants.TIME_TO_EXPIRE;
import static az.dev.smallbankingapp.properties.TestConstants.TIME_TO_LIVE;
import static az.dev.smallbankingapp.properties.TestConstants.TIME_TO_RESEND;
import static az.dev.smallbankingapp.properties.TestConstants.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import az.dev.smallbankingapp.client.SmsClient;
import az.dev.smallbankingapp.client.model.SmsDto;
import az.dev.smallbankingapp.client.model.SmsRequest;
import az.dev.smallbankingapp.config.properties.OtpProperties;
import az.dev.smallbankingapp.domain.Otp;
import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.mapper.OtpMapper;
import az.dev.smallbankingapp.repository.cache.OtpRepository;
import az.dev.smallbankingapp.util.OtpProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    private static final Integer MAX_NUMBER_OF_SENDING = 3;
    private static final Integer MAX_ATTEMPTS = 3;
    private static final String DYNAMIC_OTP_BODY = "OTP Code: " + OTP_CODE;

    @Mock
    private SmsClient smsClient;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private OtpProvider otpProvider;

    @Mock
    private OtpProperties otpProperties;

    @Mock
    private OtpMapper otpMapper;

    @InjectMocks
    private OtpService otpService;

    @Test
    void send_WhenOtpFound_Update() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);
        otpRequest.setMessageTemplate("OTP Code: ");
        SmsDto smsDto = SmsDto.withSingleDest(GSM_NUMBER, DYNAMIC_OTP_BODY);
        final var smsRequest = SmsRequest.of(smsDto);

        var otp = new Otp();
        otp.setNumberOfSending(FIRST_TIME_SENDING);
        otp.setAttempts(ATTEMPTS);
        otp.setSentTime(LocalDateTime.of(2022, 12, 14, 12, 0));

        //when
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));
        when(otpProperties.getOtpLength()).thenReturn(OTP_LENGTH);
        when(otpProvider.generate(OTP_LENGTH)).thenReturn(OTP_CODE);
        when(otpProperties.getMaxNumberOfSending()).thenReturn(MAX_NUMBER_OF_SENDING);
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_NUMBER_OF_SENDING);
        when(otpProperties.getTimeToLive()).thenReturn(TIME_TO_LIVE);
        doNothing().when(smsClient).send(smsRequest);
        doNothing().when(otpRepository).save(ID, otp, TIME_TO_LIVE);

        //then
        otpService.send(otpRequest);

        verify(otpRepository, times(1)).findById(ID);
        verify(otpProvider, times(1)).generate(OTP_LENGTH);
        verify(smsClient, times(1)).send(smsRequest);
        verify(otpRepository, times(1)).save(ID, otp, TIME_TO_LIVE);
    }

    @Test
    void send_WhenOtpNotFound_SaveNew() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);
        otpRequest.setMessageTemplate("OTP Code: ");

        SmsDto smsDto = SmsDto.withSingleDest(GSM_NUMBER, DYNAMIC_OTP_BODY);
        final var smsRequest = SmsRequest.of(smsDto);

        var otp = new Otp();

        otp.setNumberOfSending(NUMBER_OF_SENDING);

        //when
        when(otpRepository.findById(ID)).thenReturn(Optional.empty());
        when(otpProperties.getTimeToResend()).thenReturn(TIME_TO_RESEND);
        when(otpProperties.getOtpLength()).thenReturn(OTP_LENGTH);
        when(otpProperties.getTimeToLive()).thenReturn(TIME_TO_LIVE);
        when(otpProvider.generate(OTP_LENGTH)).thenReturn(OTP_CODE);
        when(otpMapper.toOtp(otpRequest)).thenReturn(otp);
        doNothing().when(smsClient).send(smsRequest);
        doNothing().when(otpRepository).save(ID, otp, TIME_TO_LIVE);

        //then
        otpService.send(otpRequest);

        verify(otpRepository, times(1)).findById(ID);
        verify(otpProvider, times(1)).generate(OTP_LENGTH);
        verify(smsClient, times(1)).send(smsRequest);
        verify(otpRepository, times(1)).save(ID, otp, TIME_TO_LIVE);
    }

    @Test
    void send_WhenMaxNumberOfSendingExceeded_ShouldThrowServiceException() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();
        otp.setNumberOfSending(MAX_NUMBER_OF_SENDING);

        //when
        when(otpProperties.getMaxNumberOfSending()).thenReturn(MAX_NUMBER_OF_SENDING);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));

        //then
        var actual = assertThrows(ServiceException.class, () -> otpService.send(otpRequest));

        assertEquals(ErrorCodes.OTP_VERIFICATION_BLOCKED.code(), actual.getCode());

        verify(otpRepository, times(1)).findById(ID);
        verify(otpProperties, times(1)).getMaxNumberOfSending();
    }

    @Test
    void send_WhenMaxAttemptsExceeded_ShouldThrowServiceException() {
        //given
        final var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();
        otp.setNumberOfSending(FIRST_TIME_SENDING);
        otp.setAttempts(MAX_ATTEMPTS);

        //when
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_ATTEMPTS);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));

        //then
        var actual = assertThrows(ServiceException.class, () -> otpService.send(otpRequest));

        assertEquals(ErrorCodes.OTP_VERIFICATION_BLOCKED.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
    }

    @Test
    void send_WhenOtpResendUnavailable_ShouldThrowServiceException() {
        //given
        var otp = new Otp();
        otp.setNumberOfSending(FIRST_TIME_SENDING);
        otp.setAttempts(ATTEMPTS);
        otp.setSentTime(SENT_TIME);

        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        otpProperties.setMaxNumberOfVerifying(MAX_ATTEMPTS);

        //when
        when(otpProperties.getMaxNumberOfSending()).thenReturn(MAX_NUMBER_OF_SENDING);
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_ATTEMPTS);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));

        //then
        var actual = assertThrows(ServiceException.class, () -> otpService.send(otpRequest));

        assertEquals(ErrorCodes.OTP_RESEND_UNAVAILABLE.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
    }

    @Test
    void verify_Success() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setOtpCode(OTP_CODE);
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();

        otp.setPhoneNumber(GSM_NUMBER);
        otp.setOtpCode(OTP_CODE);
        otp.setUuid(UUID);
        otp.setNumberOfSending(NUMBER_OF_SENDING);
        otp.setAttempts(ATTEMPTS);
        otp.setSentTime(SENT_TIME);

        //when
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_ATTEMPTS);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));
        doNothing().when(otpRepository).deleteById(ID);

        //then
        otpService.verify(otpRequest);

        verify(otpRepository, times(1)).findById(ID);
        verify(otpRepository, times(1)).deleteById(ID);
    }

    @Test
    void verify_WhenOtpNotFound_ShouldThrowServiceException() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        //when
        when(otpRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        var actual = assertThrows(ServiceException.class,
                () -> otpService.verify(otpRequest));

        assertEquals(ErrorCodes.OTP_NOT_FOUND.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
    }

    @Test
    void verify_WhenUuidInvalid_ShouldThrowServiceException() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();
        otp.setUuid("random-uuid");

        //when
        when(otpRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        var actual = assertThrows(ServiceException.class,
                () -> otpService.verify(otpRequest));

        assertEquals(ErrorCodes.OTP_NOT_FOUND.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(otpRepository);
        verifyNoInteractions(otpProperties, otpProvider, otpMapper, smsClient);
    }

    @Test
    void verify_WhenMaxAttemptsExceeded_ShouldThrowServiceException() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();

        otp.setUuid(UUID);
        otp.setAttempts(MAX_ATTEMPTS);

        //when
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_ATTEMPTS);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));

        //then
        var actual = assertThrows(ServiceException.class,
                () -> otpService.verify(otpRequest));

        assertEquals(ErrorCodes.OTP_VERIFICATION_BLOCKED.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
    }

    @Test
    void verify_WhenOtpTimeExpired_ShouldThrowServiceException() {
        //given
        var otpRequest = new OtpRequest();
        otpRequest.setGsmNumber(GSM_NUMBER);

        var otp = new Otp();

        otp.setUuid(UUID);
        otp.setAttempts(ATTEMPTS);
        otp.setSentTime(LocalDateTime.of(2022, 12, 14, 12, 0));

        //when
        when(otpProperties.getMaxNumberOfVerifying()).thenReturn(MAX_ATTEMPTS);
        when(otpProperties.getTimeToExpire()).thenReturn(TIME_TO_EXPIRE);
        when(otpRepository.findById(ID)).thenReturn(Optional.of(otp));

        //then
        var actual = assertThrows(ServiceException.class,
                () -> otpService.verify(otpRequest));

        assertEquals(ErrorCodes.OTP_TIME_EXPIRED.code(), actual.getCode());
        verify(otpRepository, times(1)).findById(ID);
    }

}
