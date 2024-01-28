package az.dev.smallbankingapp.repository;

import az.dev.smallbankingapp.entity.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends GenericRepository<Payment> {
}
