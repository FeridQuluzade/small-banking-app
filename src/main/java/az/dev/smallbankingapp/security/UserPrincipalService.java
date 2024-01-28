package az.dev.smallbankingapp.security;

import az.dev.smallbankingapp.entity.User;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByGsmNumber(username)
                .orElseThrow(
                        () -> ServiceException.of(ErrorCodes.USER_NOT_FOUND, "User not found"));

        return UserPrincipal.from(user);
    }

    public UserDetails loadById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> ServiceException.of(ErrorCodes.USER_NOT_FOUND, "User not found"));

        return UserPrincipal.from(user);
    }

}
