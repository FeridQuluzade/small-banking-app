package az.dev.smallbankingapp.repository;

import az.dev.smallbankingapp.entity.CustomerAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountsRepository extends GenericRepository<CustomerAccount> {
}
