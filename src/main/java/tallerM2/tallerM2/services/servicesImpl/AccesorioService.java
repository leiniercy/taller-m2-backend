package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.repository.AccesorioRepository;
import tallerM2.tallerM2.repository.FileRepository;
import tallerM2.tallerM2.services.IAccesorioService;
import tallerM2.tallerM2.utils.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class AccesorioService implements IAccesorioService {
    @Autowired
    AccesorioRepository accesorioRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    FileRepository fileRepository;


    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Accesorio
     */
    @Override
    public Accesorio findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Accesorio> opt = accesorioRepository.findById(id);
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
        return accesorioRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Product>
     */
    @Override
    public List<Accesorio> findAllByOrderByIdAsc() {
        return accesorioRepository.findAllByOrderByIdAsc();
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
    public Accesorio save(List<MultipartFile> files, String name, int price, int cant) throws Conflict, BadRequest, IOException {

        Optional<Accesorio> op = accesorioRepository.findByNameAndCantAndPrice(name,cant,price);
        if (op.isPresent()) {
            throw new Conflict("This object is aviable");
        }
        Accesorio accesorio = new Accesorio();
        accesorio.setName(name);
        accesorio.setPrice(price);
        accesorio.setCant(cant);
        Accesorio newAccesorio = accesorioRepository.save(Util.convertToDto(accesorio, Accesorio.class));
        //Asignacion de sus imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/accesorio/image/" + f.getName());
            f.setAccesorio(newAccesorio);
            fileRepository.save(f);
            images.add(f);
        }
        newAccesorio.setFiles(images);
        return newAccesorio;

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
    public Accesorio update(List<MultipartFile> files, String name, int price, int cant, Long id) throws ValueNotFound, BadRequest, IOException{
        Optional<Accesorio> op = accesorioRepository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }

        Accesorio to = new Accesorio();
        to.setId(id);
        to.setName(name);
        to.setPrice(price);
        to.setCant(cant);
        Accesorio accesorio = accesorioRepository.save(Util.convertToDto(to, Accesorio.class));

        //Primero elimino la relacion con todas las imagenes
        fileRepository.deleteAll(accesorio.getFiles());
        for (File file : accesorio.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }

        //Actualizo con la nueva lista de imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/accesorio/image/" + f.getName());
            f.setAccesorio(accesorio);
            fileRepository.save(f);
            images.add(f);
        }
        accesorio.setFiles(images);
        return accesorio;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param Accesorio que se quiere eliminar
     * @return Producto
     */
    @Override
    public Accesorio delete(Accesorio p) throws ValueNotFound, BadRequest {
        Optional<Accesorio> op = accesorioRepository.findById(p.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }

        Accesorio accesorio = accesorioRepository.getById(p.getId());
        for (File file : accesorio.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        accesorioRepository.delete(p);
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
        Optional<Accesorio> op = accesorioRepository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Accesorio not found");
        }

        Accesorio accesorio = accesorioRepository.getById(id);
        for (File file : accesorio.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        accesorioRepository.deleteById(id);
        return accesorio;
    }

    @Override
    public List<Accesorio> deleteAll(List<Accesorio> accesorios) throws ValueNotFound, BadRequest {
        for (Accesorio accesorio : accesorios)
            for (File file : accesorio.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        accesorioRepository.deleteAll(accesorios);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        return accesorioRepository.count();
    }
}
