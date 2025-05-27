package bo.edu.uagrm.sw1parcial2.dto;

import bo.edu.uagrm.sw1parcial2.model.RoleName;
import lombok.Data;

@Data
public class RoleResponseDTO {

    private Long id;
    private RoleName name;
}
