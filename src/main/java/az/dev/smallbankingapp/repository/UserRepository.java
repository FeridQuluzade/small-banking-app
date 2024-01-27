package az.dev.smallbankingapp.repository;

import az.dev.smallbankingapp.entity.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {

    Optional<User> findUserByGsmNumber(String gsmNumber);

}
