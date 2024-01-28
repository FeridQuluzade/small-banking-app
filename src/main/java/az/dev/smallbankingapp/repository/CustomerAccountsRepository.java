package az.dev.smallbankingapp.repository;

import az.dev.smallbankingapp.entity.AccountBalanceProjection;
import az.dev.smallbankingapp.entity.CustomerAccount;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountsRepository extends GenericRepository<CustomerAccount> {

    @Modifying
    @Query("UPDATE CustomerAccount ca SET ca.balance = :newBalance " +
            "WHERE ca.accountNumber = :accountNumber")
    void updateBalanceByAccountNumber(String accountNumber, BigDecimal newBalance);

    @Query("SELECT ca.accountNumber as accountNumber, ca.balance as balance FROM CustomerAccount " +
            "ca JOIN  ca.user WHERE ca.user.gsmNumber = :gsmNumber")
    Optional<AccountBalanceProjection> findByGsmNumber(String gsmNumber);

}
