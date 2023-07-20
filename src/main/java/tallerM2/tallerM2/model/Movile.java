package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movile extends Product {

    
    @Schema(
            description = "Pantalla of the product",
            example = "Full HD"
    )
    private String pantalla;

}
