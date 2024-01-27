package az.dev.smallbankingapp.entity;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Data
public abstract class DateAudit implements Serializable {

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

}
