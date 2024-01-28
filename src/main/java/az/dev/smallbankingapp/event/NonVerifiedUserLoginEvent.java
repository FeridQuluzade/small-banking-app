package az.dev.smallbankingapp.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NonVerifiedUserLoginEvent extends ApplicationEvent {

    public NonVerifiedUserLoginEvent(String gsmNumber) {
        super(gsmNumber);
    }

    public String getGsmNumber() {
        return (String) getSource();
    }

}
