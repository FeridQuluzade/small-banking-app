package az.dev.smallbankingapp.mapper;

import az.dev.smallbankingapp.domain.Otp;
import az.dev.smallbankingapp.dto.request.OtpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OtpMapper {

    @Mapping(target = "numberOfSending", constant = "0")
    @Mapping(target = "attempts", constant = "0")
    Otp toOtp(OtpRequest sendOtpRequest);

}