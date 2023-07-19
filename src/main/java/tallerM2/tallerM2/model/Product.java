package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public abstract class Product extends AbstractEntity {

   @Schema(description = "Name of the product", example = "Samsung galaxy A10", required = true)
   @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1]+$", message = "Use just letters and numbers")
   @Size(min = 2, max = 255, message = "Debe tener mínimo 2 caracteres")
   @Column(nullable = false)
   protected String name;
   
   @NotNull(message = "Is requeried")
   @Min(message = "Mín value is 0", value = 0)
   @Column(nullable = false)
   protected float price;

   @NotNull(message = "Is requeried")
   @Min(message = "Mín value is 0", value = 0)
   @Column(nullable = false)
   protected int cant;


}
