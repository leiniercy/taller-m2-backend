package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;


@Entity
@Table(name = "sell")
@Data
@NoArgsConstructor
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Schema(
            description = "Name of taller",
            example = "Taller 2M"
    )
    @Column(nullable = false)
    private String tallerName;

    @Schema(
            description = "date of sell",
            example = "2023-07-08"
    )
    @NotNull(message = "campo obligatorio")
    @Column(name = "sellDate",nullable = false)
    private LocalDate sellDate;

    @JoinColumn(name = "customer_id", nullable = true, updatable = true)
    @ManyToOne(optional = true)
    private Customer customer;


    @JsonIgnoreProperties({"sales"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sell_to_products",
            joinColumns =
            @JoinColumn(name = "sell_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "product_id", unique = false, nullable = true,updatable = true))
    private List<Product> products = new LinkedList<>();

}
