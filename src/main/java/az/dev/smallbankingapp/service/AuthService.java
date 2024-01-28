package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.request.LoginRequest;
import az.dev.smallbankingapp.dto.response.JwtTokenResponse;
import az.dev.smallbankingapp.entity.UserType;
import az.dev.smallbankingapp.event.NonVerifiedUserLoginEvent;
import az.dev.smallbankingapp.security.JwtTokenProvider;
import az.dev.smallbankingapp.util.RequestContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenResponse login(LoginRequest loginRequest) {
        String gsmNumber = loginRequest.getGsmNumber();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(gsmNumber, loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (UserType.NON_VERIFIED.equals(RequestContextUtil.getUserType())) {
            applicationEventPublisher.publishEvent(new NonVerifiedUserLoginEvent(gsmNumber));
            return new JwtTokenResponse(jwtTokenProvider.generateToken(authentication), false);
        }

        return new JwtTokenResponse(jwtTokenProvider.generateToken(authentication), true);
    }

}
