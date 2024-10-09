package account.model;

import account.validation.ValidPeriod;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentInModel {

    private String employee;
    @ValidPeriod
    private String period;
    @PositiveOrZero
    private Long salary;
}
