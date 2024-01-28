package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.request.UserRequest;
import az.dev.smallbankingapp.dto.response.StandardResponse;
import az.dev.smallbankingapp.entity.User;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.mapper.UserMapper;
import az.dev.smallbankingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public StandardResponse save(UserRequest userRequest) {
        User entity = userMapper.toEntity(userRequest);
        entity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(entity);
        return StandardResponse.ok();
    }

    protected User findByGsmNumber(String gsmNumber) {
        return userRepository.findUserByGsmNumber(gsmNumber)
                .orElseThrow(() -> ServiceException.of(ErrorCodes.USER_NOT_FOUND));
    }

}
