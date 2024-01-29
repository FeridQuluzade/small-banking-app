package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.client.SmsClient;
import az.dev.smallbankingapp.client.model.SmsDto;
import az.dev.smallbankingapp.client.model.SmsRequest;
import az.dev.smallbankingapp.config.properties.OtpProperties;
import az.dev.smallbankingapp.domain.Otp;
import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.dto.response.OtpResponse;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.mapper.OtpMapper;
import az.dev.smallbankingapp.repository.cache.OtpRepository;
import az.dev.smallbankingapp.util.OtpProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpMapper otpMapper;
    private final OtpProvider otpProvider;
    private final OtpRepository otpRepository;
    private final OtpProperties otpProperties;
    private final SmsClient smsClient;

    public OtpResponse send(OtpRequest otpRequest) {
        Otp otp = findOrFillFromRequest(otpRequest);

        Long timeToResend = otpProperties.getTimeToResend();

        if (!otp.isFirstTimeSending()) {
            checkOtpTemporarilyBlocked(otp, otpRequest, otpProperties);
            checkResendAvailable(otp, timeToResend);
        }

        otp.updateFrom(otpProvider.generate(otpProperties.getOtpLength()));
        otpRepository.save(otpRequest.getId(), otp, otpProperties.getTimeToLive());

        SmsDto smsDto = SmsDto.withSingleDest(otpRequest.getGsmNumber(),
                otpRequest.getMessageTemplate() + otp.getOtpCode());

        smsClient.send(SmsRequest.of(smsDto));
        log.info("OTP Message is sent to phone number: {} ", otpRequest.getGsmNumber());

        return new OtpResponse(otp.getUuid(), otpProperties.getTimeToExpire(), timeToResend);
    }

    public void verify(OtpRequest otpRequest) {
        Otp otp = findById(otpRequest);

        Integer maxNumberOfVerifying = otpProperties.getMaxNumberOfVerifying();

        checkOtpAttempts(otp, otpRequest.getGsmNumber(), maxNumberOfVerifying);

        checkOtpExpired(otp, otpProperties.getTimeToExpire());

        String otpId = otpRequest.getId();
        if (!otp.isValid(otpRequest.getOtpCode())) {
            otp.addAttempt();
            otpRepository.save(otpId, otp, otpProperties.getTimeToLive());
            checkOtpAttempts(otp, otpRequest.getGsmNumber(), maxNumberOfVerifying);
            throw ServiceException.of(ErrorCodes.INVALID_OTP);
        }

        log.info("OTP is verified for phone number: {}", otpRequest.getGsmNumber());
        otpRepository.deleteById(otpId);
    }

    private Otp findOrFillFromRequest(OtpRequest otpRequest) {
        return otpRepository.findById(otpRequest.getId())
                .orElseGet(() -> otpMapper.toOtp(otpRequest));
    }

    private Otp findById(OtpRequest otpRequest) {
        return otpRepository.findById(otpRequest.getId())
                .orElseThrow(() -> exOtpNotFound(otpRequest));
    }

    private void checkOtpTemporarilyBlocked(
            Otp otp, OtpRequest otpRequest, OtpProperties otpProperties) {

        if (otp.isTemporarilyBlocked(otpProperties.getMaxNumberOfSending(),
                otpProperties.getMaxNumberOfVerifying())) {
            throw ServiceException.of(ErrorCodes.OTP_VERIFICATION_BLOCKED,
                    otpRequest.getGsmNumber());
        }
    }

    private void checkResendAvailable(Otp otp, long timeToResend) {
        if (!otp.isTimeElapsed(timeToResend)) {
            throw ServiceException.of(ErrorCodes.OTP_RESEND_UNAVAILABLE, timeToResend);
        }
    }

    private void checkOtpAttempts(Otp otp, String phoneNumber, Integer maxNumberOfVerifying) {
        if (otp.isMaxAttemptsExceeded(maxNumberOfVerifying)) {
            throw ServiceException.of(ErrorCodes.OTP_VERIFICATION_BLOCKED, phoneNumber);
        }
    }

    private void checkOtpExpired(Otp otp, long timeToExpire) {
        if (otp.isTimeElapsed(timeToExpire)) {
            throw ServiceException.of(ErrorCodes.OTP_TIME_EXPIRED);
        }
    }

    private ServiceException exOtpNotFound(OtpRequest verifyOtpRequest) {
        return ServiceException.of(ErrorCodes.OTP_NOT_FOUND, verifyOtpRequest.getGsmNumber());
    }

}
