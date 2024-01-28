package az.dev.smallbankingapp.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.ElementCollection;
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

    @ElementCollection
    private List<Long> refPaymentIds;

    private BigDecimal refAmount;

    public void addRefPaymentIds(Long id) {
        refPaymentIds.add(id);
    }

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
                Objects.equals(getRefPaymentIds(), payment.getRefPaymentIds()) &&
                Objects.equals(getRefAmount(), payment.getRefAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPaymentType(), getAmount(),
                getRefPaymentIds(),
                getRefAmount());
    }

}
