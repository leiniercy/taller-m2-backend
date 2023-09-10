package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Schema(
            description = "Name of customer",
            example = "Juan Diaz Perez"
    )
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$", message = "Nombre del cliente incorrecto")
    @Column(nullable = false)
    protected String customerName;

    @Schema(
            description = "Movile number of customer",
            example = "58503871"
    )
    @Column(nullable = false)
    @Pattern(regexp = "^[0-9]{8}$", message = "Número de teléfono incorrecto")
    protected String customerMovile;

    @Schema(
            description = "List of sales"
    )
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sell> sales = new LinkedList<>();
}
