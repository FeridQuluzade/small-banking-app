package az.dev.smallbankingapp.service;

import static az.dev.smallbankingapp.error.model.ErrorCodes.USER_NOT_FOUND;
import static az.dev.smallbankingapp.properties.TestConstants.GSM_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import az.dev.smallbankingapp.dto.request.UserRequest;
import az.dev.smallbankingapp.dto.response.StandardResponse;
import az.dev.smallbankingapp.entity.User;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.mapper.UserMapper;
import az.dev.smallbankingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void save_Success() {
        //given
        var userRequest = new UserRequest();
        userRequest.setPassword("ferid1234Q");
        userRequest.setGsmNumber(GSM_NUMBER);

        var password = "sadfas";
        var user = new User();
        user.setGsmNumber(GSM_NUMBER);
        user.setPassword(password);

        //when
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn(password);
        when(userRepository.save(user)).thenReturn(user);

        //then
        var actual = userService.save(userRequest);

        assertEquals(StandardResponse.ok(), actual);
        verify(userMapper, times(1)).toEntity(userRequest);
        verify(passwordEncoder, times(1)).encode(userRequest.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByGsmNumber_Success() {
        //given
        var user = new User();
        user.setGsmNumber(GSM_NUMBER);

        //when
        when(userRepository.findUserByGsmNumber(GSM_NUMBER)).thenReturn(Optional.of(user));

        //then
        var actual = userService.findByGsmNumber(GSM_NUMBER);

        assertEquals(user, actual);
        verify(userRepository, times(1)).findUserByGsmNumber(GSM_NUMBER);
    }

    @Test
    void findByGsmNumber_WhenUserNotFound_ShouldThrowServiceException() {
        //when
        when(userRepository.findUserByGsmNumber(GSM_NUMBER)).thenReturn(Optional.empty());

        //then
        var exception = assertThrows(ServiceException.class,
                () -> userService.findByGsmNumber(GSM_NUMBER));

        assertTrue(exception.is(USER_NOT_FOUND));
        verify(userRepository, times(1)).findUserByGsmNumber(GSM_NUMBER);
    }

}
