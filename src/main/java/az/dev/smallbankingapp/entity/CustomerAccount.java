package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_accounts")
public class CustomerAccount extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "customer_account_seq_gen", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(
//            name = "customer_account_seq_gen",
//            sequenceName = "customer_account_seq",
//            allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance;

    @Column(unique = true, nullable = false)
    private String accountNumber;

}