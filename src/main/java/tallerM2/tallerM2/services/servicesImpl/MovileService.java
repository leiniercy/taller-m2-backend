package tallerM2.tallerM2.services.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.model.Movile;
import tallerM2.tallerM2.repository.MovileRepository;
import tallerM2.tallerM2.services.IMovileService;
import tallerM2.tallerM2.utils.Util;


@Service
public class MovileService implements IMovileService {

    @Autowired
    private MovileRepository movileRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileService fileService;

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return una referencia a la entidad que coincida con el identificador.
     */
    @Override
    public Movile findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Movile> opt = movileRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ValueNotFound("Movile not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Movile>
     */
    @Override
    public List<Movile> findAll() {
        return movileRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Movile>
     */
    @Override
    public List<Movile> findAllByOrderByIdAsc() {
        return movileRepository.findAllByOrderByIdAsc();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param files         listado de imagenes
     * @param name          nombre del producto
     * @param price         precio del producto
     * @param cant          cant de productos
     * @param sizeStorage   capacidad de almacenaniento de producto
     * @param ram           velocidad del procesador
     * @param camaraTrasera cantidad de pixeles de la camara trasera
     * @param camaraFrontal cantidad de pixeles de la camara frontal
     * @param banda2G       tipo de conexion que accepta
     * @param banda3G       tipo de conexion que accepta
     * @param banda4G       tipo de conexion que accepta
     * @param banda5G       tipo de conexion que accepta
     * @param bateria       duracion de la bateria en dias
     * @return Movile
     */
    @Override
    public Movile save(List<MultipartFile> files, String name, int price, int cant,
                       int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
                       boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G, long bateria)
            throws Conflict, BadRequest, IOException {

        Optional<Movile> op = movileRepository.findByNameAndPriceAndCantAndSizeStorageAndRamAndCamaraTraseraAndCamaraFrontalAndBanda2GAndBanda3GAndBanda4GAndBanda5GAndBateria(name, price, cant,
                sizeStorage, ram, camaraTrasera, camaraFrontal,
                banda2G, banda3G, banda4G, banda5G, bateria);
        if (op.isPresent()) {
            throw new Conflict("This movile is aviable");
        }
        Movile movile = new Movile();
        movile.setName(name);
        movile.setPrice(price);
        movile.setCant(cant);
        movile.setSizeStorage(sizeStorage);
        movile.setRam(ram);
        movile.setCamaraTrasera(camaraTrasera);
        movile.setCamaraFrontal(camaraFrontal);
        movile.setBanda2G(banda2G);
        movile.setBanda3G(banda3G);
        movile.setBanda4G(banda4G);
        movile.setBanda5G(banda5G);
        movile.setBateria(bateria);
        Movile m = movileRepository.save(Util.convertToDto(movile, Movile.class));
        //Asignacion de sus imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/movile/image/" + f.getName());
            f.setProduct(m);
            images.add(fileService.save(f));
        }
        m.setFiles(images);
        return m;
    }

    /**
     * ACUTAULIZAR LA INFORMACION TODA LA INFORMACION DE UN OBJETO
     *
     * @param id            identificador del producto
     * @param files         listado de imagenes
     * @param name          nombre del producto
     * @param price         precio del producto
     * @param cant          cant de productos
     * @param sizeStorage   capacidad de almacenaniento de producto
     * @param ram           velocidad del procesador
     * @param camaraTrasera cantidad de pixeles de la camara trasera
     * @param camaraFrontal cantidad de pixeles de la camara frontal
     * @param banda2G       tipo de conexion que accepta
     * @param banda3G       tipo de conexion que accepta
     * @param banda4G       tipo de conexion que accepta
     * @param banda5G       tipo de conexion que accepta
     * @param bateria       duracion de la bateria en dias
     * @return Movile
     */
    @Override
    public Movile update(List<MultipartFile> files, String name, int price, int cant,
                         int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
                         boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G
            , long bateria, Long id) throws ValueNotFound, BadRequest, IOException {

        Optional<Movile> op = movileRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Movile not found");
        }

        //Creo uno nuevo con sus mismas caracteristicas
        Movile movile = movileRepository.getById(id);
        movile.setName(name);
        movile.setPrice(price);
        movile.setCant(cant);
        movile.setSizeStorage(sizeStorage);
        movile.setRam(ram);
        movile.setCamaraTrasera(camaraTrasera);
        movile.setCamaraFrontal(camaraFrontal);
        movile.setBanda2G(banda2G);
        movile.setBanda3G(banda3G);
        movile.setBanda4G(banda4G);
        movile.setBanda5G(banda5G);
        movile.setBateria(bateria);

        //Eliminando todos las imagenes vinculadas a este movile
        for (File file : movile.getFiles()) {
            imageService.eliminarImagen(file.getName());
            fileService.deleteById(file.getId());
        }

        Movile m = movileRepository.save(Util.convertToDto(movile, Movile.class));

        //Actualizo el movile con la nueva lista de imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/movile/image/" + f.getName());
            f.setProduct(m);
            images.add(fileService.save(f));
        }
        m.setFiles(images);

        return m;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param m objeto que se quiere eliminar
     * @return Movile
     */
    @Override
    public Movile delete(Movile m) throws ValueNotFound, BadRequest {
        Optional<Movile> op = movileRepository.findById(m.getId());
        if (op.isEmpty()) {
            throw new ValueNotFound("Movile not found");
        }

        Movile movile = movileRepository.getById(m.getId());
        for (File file : movile.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        movileRepository.delete(m);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param id identificador del objeto
     * @return Movile
     */
    @Override
    public Movile deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Movile> op = movileRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Movile not found");
        }

        Movile movile = movileRepository.getById(id);
        for (File file : movile.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        movileRepository.deleteById(id);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param moviles listado de objetos que se quieren eliminar
     * @return List<Movile>  objetos que aun existen en la BD
     */
    @Override
    public List<Movile> deleteAll(List<Movile> moviles) throws ValueNotFound, BadRequest {
        for (Movile movile : moviles)
            for (File file : movile.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        movileRepository.deleteAll(moviles);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        return movileRepository.count();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE DADO UN NOMBRE
     *
     * @return long
     */
    @Override
    public Long countByName(String name) {
        return movileRepository.countByName(name);
    }

}
