package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@DiscriminatorValue("charger")
public class Charger extends Product {

    @Schema(
            description = "Tipo de conector",
            example = "USB Type C"
    )
    @Column(nullable = false)
    private String connectorType;
    @Schema(
            description = "Dispositivos compatibles",
            example = "Samsung"
    )
    @Column(nullable = false)
    private String compatibleDevice;

}
