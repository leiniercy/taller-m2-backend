/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
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
//@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product implements Comparable<Product> {

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



    @Override
    public int compareTo(Product prod) {
        return Long.compare(this.getId(), prod.getId());
    }


}
