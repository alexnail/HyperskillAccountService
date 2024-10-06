package account.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupModel {
    @NotEmpty
    private String name;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@acme.com$",
            message = "Invalid email format"
    )
    private String email;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
