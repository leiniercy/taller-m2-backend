package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "sell")
@Data
@NoArgsConstructor
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(
            description = "description",
            example = "description"
    )
    @Column(nullable = false)
    private String description;


    @Schema(
            description = "sale price",
            example = "121"
    )
    @Column(nullable = false)
    private int salePrice;

    @Schema(
            description = "Name of taller",
            example = "Taller 2M"
    )
    @Column(nullable = false)
    private String tallerName;

    @Schema(
            description = "Name of user",
            example = "user"
    )
    @Column(nullable = false)
    private String username;

    @Schema(
            description = "date of sell",
            example = "2023-07-08"
    )
    @NotNull(message = "campo obligatorio")
    @Column(name = "sellDate", nullable = false)
    private LocalDate sellDate;

    @JoinColumn(name = "customer_id", nullable = true, updatable = true)
    @ManyToOne(optional = true)
    private Customer customer;

    @JsonIgnoreProperties({"sales"})
    @JoinColumn(name = "product_id", nullable = true, updatable = true)
    @ManyToOne(optional = true)
    private Product product;
    @Schema(
            description = " product cant",
            example = "3"
    )
    @Min(value = 0, message = "Cantidad incorrecta, valor mínimo 0")
    @Column(nullable = false)
    private  int cantProduct;



}
