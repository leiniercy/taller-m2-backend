package tallerM2.tallerM2.utils.dto;

import lombok.Data;

@Data
public class UserEditRequest {
    private String username;
    private String email;
    private String taller;
    private String rol;
}
