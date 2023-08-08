package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(nullable = false)
    protected String customerName;

    @Schema(
            description = "Movile number of customer",
            example = "58503871"
    )
    @Column(nullable = false)
    protected String customerMovile;

    @Schema(
            description = "List of sales"
    )
    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sell> sales = new LinkedList<>();
}
