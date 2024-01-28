package az.dev.smallbankingapp.controller;

import az.dev.smallbankingapp.dto.request.LoginRequest;
import az.dev.smallbankingapp.dto.request.UserRequest;
import az.dev.smallbankingapp.dto.response.JwtTokenResponse;
import az.dev.smallbankingapp.dto.response.StandardResponse;
import az.dev.smallbankingapp.service.AuthService;
import az.dev.smallbankingapp.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public StandardResponse register(@Valid @RequestBody UserRequest request) {
        return userService.save(request);
    }

    @PostMapping("/login")
    public JwtTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}
