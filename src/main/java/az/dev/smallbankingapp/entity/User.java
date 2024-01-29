package az.dev.smallbankingapp.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "user_seq_gen", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(
//            name = "user_seq_gen",
//            sequenceName = "user_seq",
//            allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String gsmNumber;

    @Enumerated(value = EnumType.STRING)
    private UserType userType = UserType.NON_VERIFIED;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname(), user.getSurname()) &&
                Objects.equals(getGsmNumber(), user.getGsmNumber()) &&
                getUserType() == user.getUserType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPassword(), getName(), getSurname(),
                getGsmNumber(), getUserType());
    }

}
