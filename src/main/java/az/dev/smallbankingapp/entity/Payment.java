package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;
import java.util.Objects;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private BigDecimal amount;
    private String source;
    private String destination;
    private BigDecimal amountAfterRef;
    private Long originalPaymentId;

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