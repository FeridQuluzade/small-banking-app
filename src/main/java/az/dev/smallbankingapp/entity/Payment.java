package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "payments_seq_gen", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(
//            name = "payments_seq_gen",
//            sequenceName = "payments_seq",
//            allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(length = 40)
    private String source;

    @Column(length = 40)
    private String destination;
    private Long originalPaymentId;

    private BigDecimal amount;
    private BigDecimal amountAfterRef;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) &&
                getPaymentType() == payment.getPaymentType() &&
                Objects.equals(getAmount(), payment.getAmount()) &&
                Objects.equals(getSource(), payment.getSource()) &&
                Objects.equals(getDestination(), payment.getDestination()) &&
                Objects.equals(getAmountAfterRef(), payment.getAmountAfterRef()) &&
                Objects.equals(getOriginalPaymentId(), payment.getOriginalPaymentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPaymentType(), getAmount(), getSource(),
                getDestination(), getAmountAfterRef(), getOriginalPaymentId());
    }

}