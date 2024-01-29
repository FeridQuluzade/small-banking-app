package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;

public interface PaymentBalanceProjection {

    Long getId();

    BigDecimal getAmountAfterRef();

    PaymentType getPaymentType();

    default boolean is(PaymentType paymentType) {
        return paymentType.equals(getPaymentType());
    }

    default BigDecimal subtractRefAmount(BigDecimal amount) {
        return getAmountAfterRef().subtract(amount);
    }

}
