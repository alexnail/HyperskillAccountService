package account.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<ValidPeriod, String> {
    private static final String PERIOD_REGEX = "^(0[1-9]|1[0-2])-(19|20)\\d{2}$";

    @Override
    public boolean isValid(String period, ConstraintValidatorContext context) {
        return period != null && period.matches(PERIOD_REGEX);
    }
}
