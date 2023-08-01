package tallerM2.tallerM2.configuration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {
    private String username;
    private List<String> roles;
}
