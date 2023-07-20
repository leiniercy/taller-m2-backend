package tallerM2.tallerM2.services.servicesImpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.bytebuddy.asm.Advice.Return;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Movile;
import tallerM2.tallerM2.repository.MovileRepository;
import tallerM2.tallerM2.services.IMovileService;
import tallerM2.tallerM2.utils.Util;

@Transactional
@Service
@RequiredArgsConstructor
public class MovileService implements IMovileService {

    public final MovileRepository repository;

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return una referencia a la entidad que coincida con el identificador.
     *
     */
    @Override
    public Movile findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Movile> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ValueNotFound("Movile not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return un listado de objetos
     *
     */
    @Override
    public List<Movile> findAll() {
        return repository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return un listado de objetos
     *
     */
    @Override
    public List<Movile> findAllByOrderByIdAsc() {
        return repository.findAllByOrderByIdAsc();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param entity indica el objeto que se desea guradar. No debe ser vacio
     * {@literal null}.
     * @return el objeto guardado
     *
     */
    @Override
    public Movile save(Movile m) throws Conflict, BadRequest {
        Optional op = repository.findById(m.getId());
        if (op.isPresent()) {
            throw new Conflict("This movile already exists");
        }
        Movile movile = Util.convertToDto(m, Movile.class);
        return repository.save(movile);
    }

    /**
     * Map todo
     *
     * @param from
     * @return to
     *
     */
    @Override
    public Movile update(Movile from, Long id) throws ValueNotFound, BadRequest {

        Optional<Movile> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Movile not found");
        }
        Movile to = Util.convertToDto(from, Movile.class);

        return repository.save(to);
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param Objeto
     * @return Movile
     *
     */
    @Override
    public Movile delete(Movile m) throws ValueNotFound, BadRequest {
        Optional<Movile> op = repository.findById(m.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Movile not found");
        }

        Movile movile = repository.getById(m.getId());
        repository.delete(m);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param id
     * @return Movile
     *
     */
    @Override
    public Movile deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Movile> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Movile not found");
        }

        Movile movile = repository.getById(id);
        repository.deleteById(id);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param List<Object>
     *
     */
    @Override
    public void deleteAll(List<Movile> moviles) {
        repository.deleteAll(moviles);
    }

//  METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
    @Override
    public long count() {
        return (long) repository.count();
    }

//  METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE DADO UN NOMBRE
    @Override
    public Long countByName(String name) {
        return repository.countByName(name);
    }

}
