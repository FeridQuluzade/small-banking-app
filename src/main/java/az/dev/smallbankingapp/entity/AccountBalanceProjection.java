package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;

public interface AccountBalanceProjection {

    String getAccountNumber();

    BigDecimal getBalance();

    default BigDecimal subtractBalance(BigDecimal balance) {
        return getBalance().subtract(balance);
    }

}
