package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Accesorio extends AbstractEntity {

    @Schema(
            description = "Name of the product",
            example = "Samsung Galaxy A10"
    )
    private String name;

    @Schema(
            description = "Price of the product",
            example = "10"
    )
    private int price;

    @Schema(
            description = "Cant of products",
            example = "10"
    )
    private int cant;

    @Schema(
            description = "List of images of the product",
            example = "logo1.png, logo2.png"
    )

//    @OneToMany(targetEntity = File.class)
//    private List<File> files;
    @OneToMany(mappedBy = "accesorio", cascade = CascadeType.ALL)
    private List<File> files = new LinkedList<>();

}
