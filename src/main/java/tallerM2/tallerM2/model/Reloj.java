package tallerM2.tallerM2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @Column(nullable = false)
    private String specialFeature;

    @Schema(
            description = "Dispositivos compatibles",
            example = "Samsung"
    )
    @Column(nullable = false)
    private String compatibleDevice;

    @Schema(
            description = "Tiempo de la bateria en dias",
            example = "15"
    )
    @Column(nullable = false)
    private int bateryLife;

    @Schema(
            description = "List of images of the product",
            example = "logo1.png, logo2.png"
    )

    @OneToMany(mappedBy = "reloj", cascade = CascadeType.ALL)
    protected List<File> files = new LinkedList<>();
}
