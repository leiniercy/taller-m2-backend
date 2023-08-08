package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
//@DiscriminatorValue("movile")
public class Movile extends Product {


    //almacenamiento
    @Schema(
            description = "capacidad de almacenamiento",
            example = "128"
    )
    @Column(nullable = false)
    private int sizeStorage;
    @Schema(
            description = "Ram del dsipositivo",
            example = "128"
    )
    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 0, message = "el valor mínimo es 0")
    @Column(nullable = false)
    private int ram;

    //Camaras
    @Schema(
            description = "Cant de pixeles de la camara trasera",
            example = "16"
    )
    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 0, message = "el valor mínimo es 0")
    @Column(nullable = false)
    private int camaraTrasera;

    @Schema(
            description = "Cant de pixeles de la camara frontal",
            example = "4"
    )
    @NotNull(message = "Este campo es obligatorio")
    @Min(value = 0, message = "el valor mínimo es 0")
    @Column(nullable = false)
    private int camaraFrontal;

    //Conectividad
    @Schema(
            description = "Banda GSM (2G)",
            example = "true"
    )
    @Column(nullable = false)
    private boolean banda2G; //GSM

    @Schema(
            description = "Banda LATAM/UMTS (3G)",
            example = "true"
    )
    @Column(nullable = false)
    private boolean banda3G;

    @Schema(
            description = "Banda LTE/LATAM (4G)",
            example = "true"
    )
    @Column(nullable = false)
    private boolean banda4G;

    @Schema(
            description = "Banda (5G)",
            example = "false"
    )
    @Column(nullable = false)
    private boolean banda5G;

    //Bateria
    @Schema(
            description = "Duracion aproximada de la baeteria en horas",
            example = "24"
    )
    @Column(nullable = false)
    private long bateria;

}
