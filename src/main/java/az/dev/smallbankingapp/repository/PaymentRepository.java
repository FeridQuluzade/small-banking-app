package az.dev.smallbankingapp.repository;

import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentBalanceProjection;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends GenericRepository<Payment> {

    @Modifying
    @Query("UPDATE Payment ca SET ca.amountAfterRef = :newAmount WHERE ca.id = :id")
    void updateBalanceById(Long id, BigDecimal newAmount);

    default PaymentBalanceProjection getProjectionById(Long id) {
        return this.findProjectionById(id)
                .orElseThrow(() -> ServiceException.of(ErrorCodes.PAYMENT_NOT_FOUND));
    }

    @Query("SELECT  py.amountAfterRef as amountAfterRef," +
            " py.paymentType as paymentType," +
            " py.id as id " +
            "FROM Payment py WHERE py.id = :id")
    Optional<PaymentBalanceProjection> findProjectionById(Long id);

}
