package model.entity.simcard;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SimCard {
    private int id, lastInvoice, balance;
    private String name, phoneNumber, paymentPlan;
    private Provider provider;
    private LocalDate signupDate;
    private boolean monthly, manual;

    public String toString () {return new Gson().toJson(this);
    }
}
