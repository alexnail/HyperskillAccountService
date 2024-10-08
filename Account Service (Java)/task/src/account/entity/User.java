package account.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
}
