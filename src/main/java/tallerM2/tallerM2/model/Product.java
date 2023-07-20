/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Admin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractEntity implements Comparable<Product> {

    @Schema(
            description = "Name of the product",
            example = "Samsung Galaxy A10"
    )
    protected String name;

    @Schema(
            description = "Price of the product",
            example = "10"
    )
    protected int price;

    @Schema(
            description = "Cant of products",
            example = "10"
    )
    protected int cant;

    @Schema(
            description = "Image of the product",
            example = "logo.png"
    )

    @Lob
    @Column()
    protected String image;

    @Override
    public String toString() {
        return "Product{" + "name=" + name + ", price=" + price + ", cant=" + cant + ", image=" + image + '}';
    }

    @Override
    public int compareTo(Product prod) {
//      implementacion de la comparacion basada en el identificador
        return Long.compare(this.getId(), prod.getId());
    }

}
