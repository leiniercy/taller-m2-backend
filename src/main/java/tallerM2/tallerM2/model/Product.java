/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

import lombok.*;

/**
 *
 * @author Admin
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public /*abstract*/ class Product implements Comparable<Product> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Schema(
            description = "Name of the product",
            example = "Samsung Galaxy A10"
    )
    @Column(nullable = false)

    protected String name;

    @Schema(
            description = "Price of the product",
            example = "10"
    )
    @Column(nullable = false)

    protected int price;

    @Schema(
            description = "Cant of products",
            example = "10"
    )
    @Column(nullable = false)
    protected int cant;

    @Schema(
            description = "List of images of the product"
    )
    @JsonIgnoreProperties({"product"})
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    protected List<File> files = new LinkedList<>();

    @Schema(
            description = "List of sales of the product"
    )
    @JsonIgnoreProperties({"product"})
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Sell> sales = new LinkedList<>();

    @Override
    public int compareTo(Product prod) {
        return Long.compare(this.getId(), prod.getId());
    }


}
