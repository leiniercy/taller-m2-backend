package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class Reloj extends Product{

    @Schema(
            description = "Funcionalidades del reloj",
            example = "Time Display, Alarm Clock, GPS, Notifications, Email, Heart Rate Monitor, Text Messaging, Music Player, Sleep Monitor",
            required = true
    )
    @Pattern(regexp = "^[,.a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1\\s]+$", message = "Funcionalidades incorrectas")
    @Column(nullable = false)
    private String specialFeature;

    @Schema(
            description = "Dispositivos compatibles",
            example = "Samsung"
    )
    @Column(nullable = false)
    @Pattern(regexp = "^[,.a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1\\s]+$", message = "Dispositivos compatibles incorrectos")
    private String compatibleDevice;

    @Schema(
            description = "Bateria en amp",
            example = "2400"
    )
    @Column(nullable = false)
    @Min(value = 0, message = "Batería incorrecta, valor mínimo 0")
    private int bateryLife;

}
