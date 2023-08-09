package tallerM2.tallerM2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Schema(
            description = "File id",
            example = "1",
            required = true
    )
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    private String name;
    private String url;

    @JoinColumn(name = "product_id", nullable = true, updatable = true)
    @JsonIgnoreProperties({"files"})
    @ManyToOne(optional = true)
    protected Product product;
}
