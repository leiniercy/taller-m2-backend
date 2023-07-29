package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.repository.AccesorioRepository;
import tallerM2.tallerM2.services.IAccesorioService;
import tallerM2.tallerM2.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AccesorioService implements IAccesorioService {
    @Autowired
    AccesorioRepository repository;

    @Autowired
    ImageService imageService;

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Accesorio
     */
    @Override
    public Accesorio findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Accesorio> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Accesorio>
     */
    @Override
    public List<Accesorio> findAll() {
        return repository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Product>
     */
    @Override
    public List<Accesorio> findAllByOrderByIdAsc() {
        return repository.findAllByOrderByIdAsc();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param List<file> imagen del producto
     * @param name       nombre del producto
     * @param price      precio del producto
     * @param cant       cant de productos
     * @return Product
     */
    @Override
    public Accesorio save(Accesorio accesorio) throws Conflict, BadRequest {

//        Optional<Accesorio> op = repository.findById(accesorio.getId());
//        if (op.isPresent()) {
//            throw new Conflict("This object is aviable");
//        }

        return repository.save(Util.convertToDto(accesorio, Accesorio.class));

    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO EXISTA, SE MAPEA SU NUEVA INFORMACION,
     * Y LUEGO SE GURADA LA INFORMACION
     *
     * @param List<file> imagen del producto
     * @param name       nombre del producto
     * @param price      precio del producto
     * @param cant       cant de productos
     * @param id         cant del producto a modificar
     * @return Product
     */
    @Override
    public Accesorio update(Accesorio from, Long id) throws ValueNotFound, BadRequest {
        Optional<Accesorio> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }
        Accesorio to = new Accesorio();
        to.setId(id);
        to.setName(from.getName());
        to.setPrice(from.getPrice());
        to.setCant(from.getCant());
        to.setFiles(from.getFiles());
        return repository.save(Util.convertToDto(to, Accesorio.class));
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param Accesorio que se quiere eliminar
     * @return Producto
     */
    @Override
    public Accesorio delete(Accesorio p) throws ValueNotFound, BadRequest {
        Optional<Accesorio> op = repository.findById(p.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }

        Accesorio accesorio = repository.getById(p.getId());
        for (File file : accesorio.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        repository.delete(p);
        return accesorio;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param List<Accesorio>
     * @return List<Accesorio> ordenados asendente por ID
     */
    @Override
    public Accesorio deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Accesorio> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }

        Accesorio accesorio = repository.getById(id);
        for (File file : accesorio.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        repository.deleteById(id);
        return accesorio;
    }

    @Override
    public List<Accesorio> deleteAll(List<Accesorio> accesorios) throws ValueNotFound, BadRequest {
        for (Accesorio accesorio : accesorios)
            for (File file : accesorio.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        repository.deleteAll(accesorios);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        return repository.count();
    }
}
