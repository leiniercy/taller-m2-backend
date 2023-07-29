package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@AllArgsConstructor
@NoArgsConstructor
public class File extends AbstractEntity{
    private String name;
    private String url;

    @JoinColumn(name = "accesorio_id", nullable = false, updatable = true)
    @JsonIgnoreProperties({"files"})
    @ManyToOne(optional = false)
    protected Accesorio accesorio;
}
