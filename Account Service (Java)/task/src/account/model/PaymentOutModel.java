package account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@Builder
public class PaymentOutModel {
    private String name;
    private String lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM-yyyy")
    private YearMonth period;
    private String salary;
}
