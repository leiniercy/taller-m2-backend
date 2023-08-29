package tallerM2.tallerM2.services.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.model.Movile;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.repository.MovileRepository;
import tallerM2.tallerM2.services.IMovileService;
import tallerM2.tallerM2.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tallerM2.tallerM2.utils.MinioAdapter;

@Service
public class MovileService implements IMovileService {

    @Autowired
    private MovileRepository movileRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    MinioAdapter minioAdapter;
    @PersistenceContext
    private EntityManager em;

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
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER 2M
     *
     * @return List<Movile> listado de objetos del taller 2M
     */
    @Override
    public List<Movile> findAllTaller2M() {
        return em.createQuery("SELECT m FROM Movile m WHERE m.taller LIKE 'Taller 2M' ORDER BY m.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER MJ
     *
     * @return List<Movile> listado de objetos del taller MJ
     */
    @Override
    public List<Movile> findAllTallerMJ() {
        return em.createQuery("SELECT m FROM Movile m WHERE m.taller LIKE 'Taller MJ' ORDER BY m.name")
                .getResultList();
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
     * @param files listado de imagenes
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @param taller nombre del taller al q pertenece
     * @param sizeStorage capacidad de almacenaniento de producto
     * @param ram velocidad del procesador
     * @param camaraTrasera cantidad de pixeles de la camara trasera
     * @param camaraFrontal cantidad de pixeles de la camara frontal
     * @param banda2G tipo de conexion que accepta
     * @param banda3G tipo de conexion que accepta
     * @param banda4G tipo de conexion que accepta
     * @param banda5G tipo de conexion que accepta
     * @param bateria duracion de la bateria en dias
     * @return Movile
     */
    @Override
    public Movile save(List<MultipartFile> files, String name, int price, int cant, String taller,
            int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
            boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G, long bateria)
            throws Conflict, BadRequest, IOException {

        List<Movile> products = em.createQuery("SELECT m FROM Movile m "
                + "WHERE m.name LIKE :name AND  m.cant = :cant AND  m.price = :price AND  m.taller = :taller "
                + "AND  m.sizeStorage = :sizeStorage AND  m.ram = :ram AND  m.camaraTrasera = :camaraTrasera "
                + "AND  m.camaraFrontal = :camaraFrontal AND  m.banda2G = :banda2G AND  m.banda3G = :banda3G "
                + "AND  m.banda4G = :banda4G AND  m.banda5G = :banda5G AND  m.bateria = :bateria")
                .setParameter("name", name)
                .setParameter("cant", cant)
                .setParameter("price", price)
                .setParameter("taller", taller)
                .setParameter("sizeStorage", sizeStorage)
                .setParameter("ram", ram)
                .setParameter("camaraTrasera", camaraTrasera)
                .setParameter("camaraFrontal", camaraFrontal)
                .setParameter("banda2G", banda2G)
                .setParameter("banda3G", banda3G)
                .setParameter("banda4G", banda4G)
                .setParameter("banda5G", banda5G)
                .setParameter("bateria", bateria)
                .setMaxResults(1)
                .getResultList();
        if (!products.isEmpty()) {
            throw new Conflict("This movile is aviable");
        }
        Movile movile = new Movile();
        movile.setName(name);
        movile.setPrice(price);
        movile.setCant(cant);
        movile.setTaller(taller);
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
            String nombreImagen = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            minioAdapter.uploadFile(nombreImagen, file.getInputStream(), file.getSize());
            f.setName(nombreImagen);
            f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
            f.setProduct(m);
            images.add(fileService.save(f));
        }
        m.setFiles(images);
        return m;
    }

    /**
     * ACUTAULIZAR LA INFORMACION TODA LA INFORMACION DE UN OBJETO
     *
     * @param id identificador del producto
     * @param files listado de imagenes
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @param taller nombre del taller al q pertenece
     * @param sizeStorage capacidad de almacenaniento de producto
     * @param ram velocidad del procesador
     * @param camaraTrasera cantidad de pixeles de la camara trasera
     * @param camaraFrontal cantidad de pixeles de la camara frontal
     * @param banda2G tipo de conexion que accepta
     * @param banda3G tipo de conexion que accepta
     * @param banda4G tipo de conexion que accepta
     * @param banda5G tipo de conexion que accepta
     * @param bateria duracion de la bateria en dias
     * @return Movile
     */
    @Override
    public Movile update(List<MultipartFile> files, String name, int price, int cant, String taller,
            int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
            boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G,
            long bateria, Long id) throws ValueNotFound, BadRequest, IOException {

        Optional<Movile> op = movileRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Movile not found");
        }

        //Creo uno nuevo con sus mismas caracteristicas
        Movile movile = op.get();
        movile.setName(name);
        movile.setPrice(price);
        movile.setCant(cant);
        movile.setTaller(taller);
        movile.setSizeStorage(sizeStorage);
        movile.setRam(ram);
        movile.setCamaraTrasera(camaraTrasera);
        movile.setCamaraFrontal(camaraFrontal);
        movile.setBanda2G(banda2G);
        movile.setBanda3G(banda3G);
        movile.setBanda4G(banda4G);
        movile.setBanda5G(banda5G);
        movile.setBateria(bateria);

        //Listado de imagenes actual
        List<File> previousImages = movile.getFiles();
        //Comprobando si no son las mismas imagenes
        boolean band = true;
        if (files.size() == movile.getFiles().size()) {
            for (int i = 0; i < files.size() && band; i++) {
                if (!files.get(i).getOriginalFilename().equals(movile.getFiles().get(i).getName())) {
                    band = false;
                }
            }
        } else {
            band = false;
        }

        Movile m = movileRepository.save(movile);

        if (!band) {
            //Actualizo el movile con la nueva lista de imagenes
            List<File> images = new LinkedList<>();
            for (MultipartFile file : files) {
                File f = new File();
                String nombreImagen = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                minioAdapter.uploadFile(nombreImagen, file.getInputStream(), file.getSize());
                f.setName(nombreImagen);
                f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
                f.setProduct(m);
                images.add(fileService.save(f));
            }
            m.setFiles(images);

            //Eliminando todos las imagenes anteriores vinculadas a este movile
            for (File file : previousImages) {
                 minioAdapter.deleteFile(file.getName());
                fileService.deleteById(file.getId());
            }
        }

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
             minioAdapter.deleteFile(file.getName());
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
             minioAdapter.deleteFile(file.getName());
        }
        movileRepository.deleteById(id);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param moviles listado de objetos que se quieren eliminar
     * @return List<Movile> objetos que aun existen en la BD
     */
    @Override
    public List<Movile> deleteAll(List<Movile> moviles) throws ValueNotFound, BadRequest {
        for (Movile movile : moviles) {
            for (File file : movile.getFiles()) {
                 minioAdapter.deleteFile(file.getName());
            }
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
        Query query = em.createQuery("select coalesce(sum(m.cant),0) from Movile m");
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
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
