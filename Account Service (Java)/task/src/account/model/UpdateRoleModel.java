package account.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateRoleModel {
    private String user;
    private String role;
    private String operation; //GRANT, REMOVE
}
