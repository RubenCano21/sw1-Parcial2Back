package bo.edu.uagrm.sw1parcial2.dto;

import bo.edu.uagrm.sw1parcial2.model.RoleName;
import lombok.Data;

@Data
public class UserRoleRequest {

    private Long userId;
    private RoleName roleName;
}
