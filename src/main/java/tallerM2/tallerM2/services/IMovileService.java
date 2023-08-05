package tallerM2.tallerM2.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Movile;

public interface IMovileService {

    public Movile findById(Long id) throws ValueNotFound, BadRequest;

    public List<Movile> findAll();

    public List<Movile> findAllByOrderByIdAsc();

    public Movile save(List<MultipartFile> files, String name, int price, int cant,
                       int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
                       boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G, long bateria)
            throws Conflict, BadRequest, IOException;

    public Movile update(List<MultipartFile> files, String name, int price, int cant,
                         int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
                         boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G
            , long bateria, Long id) throws ValueNotFound, BadRequest, IOException;

    public Movile delete(Movile m) throws ValueNotFound, BadRequest;

    public Movile deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Movile> deleteAll(List<Movile> moviles) throws ValueNotFound, BadRequest;

    public long count();

    public Long countByName(String name);

}
