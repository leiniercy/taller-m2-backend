package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Charger;
import tallerM2.tallerM2.model.File;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.repository.ChargerRepository;
import tallerM2.tallerM2.services.IChargerService;
import tallerM2.tallerM2.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class ChargerService implements IChargerService {

    @Autowired
    private ChargerRepository chargerRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileService fileService;
    @PersistenceContext
    private EntityManager em;

    /**
     * METODO PARA VERIFICAR SI EL Cargador EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return una referencia a la entidad que coincida con el identificador.
     */
    @Override
    public Charger findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Charger> opt = chargerRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Charger>
     */
    @Override
    public List<Charger> findAll() {
        return chargerRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * DEL TALLER 2M
     *
     * @return List<Charger> listado de cargadores del taller 2M
     */
    @Override
    public List<Charger> findAllTaller2M() {
        return em.createQuery("SELECT c FROM Charger c WHERE c.taller LIKE 'Taller 2M' ORDER BY c.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * DEL TALLER MJ
     *
     * @return List<Charger> listado de cargadores del taller MJ
     */
    @Override
    public List<Charger> findAllTallerMJ() {
        return em.createQuery("SELECT c FROM Charger c WHERE c.taller LIKE 'Taller MJ' ORDER BY c.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Charger>
     */
    @Override
    public List<Charger> findAllByOrderByIdAsc() {
        return chargerRepository.findAllByOrderByIdAsc();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param files            listado de imagenes
     * @param name             nombre del producto
     * @param price            precio del producto
     * @param cant             cant de productos
     * @param taller           nombre del taller al q pertenece
     * @param connectorType    tipo de conector que utiliza
     * @param compatibleDevice dispositivos compatibles
     * @return Charger
     */
    @Override
    public Charger save(List<MultipartFile> files, String name, int price, int cant, String taller,
                        String connectorType, String compatibleDevice)
            throws Conflict, BadRequest, IOException {
        List<Charger> products = em.createQuery("SELECT c FROM Charger c " +
                        "WHERE c.name LIKE :name AND  c.cant = :cant AND  c.price = :price AND  c.taller = :taller " +
                        "AND  c.connectorType = :connectorType " +
                        "AND  c.compatibleDevice = :compatibleDevice")
                .setParameter("name", name)
                .setParameter("cant", cant)
                .setParameter("price", price)
                .setParameter("taller", taller)
                .setParameter("connectorType", connectorType)
                .setParameter("compatibleDevice", compatibleDevice)
                .setMaxResults(1)
                .getResultList();
        if (!products.isEmpty()) {
            throw new Conflict("This charger is aviable");
        }

        Charger charger = new Charger();
        charger.setName(name);
        charger.setPrice(price);
        charger.setCant(cant);
        charger.setTaller(taller);
        charger.setConnectorType(connectorType);
        charger.setCompatibleDevice(compatibleDevice);

        Charger c = chargerRepository.save(Util.convertToDto(charger, Charger.class));
        //Asignacion de sus imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
            f.setProduct(c);
            images.add(fileService.save(f));
        }
        c.setFiles(images);
        return c;
    }

    /**
     * ACUTAULIZAR LA INFORMACION TODA LA INFORMACION DE UN OBJETO
     *
     * @param files            listado de imagenes
     * @param name             nombre del producto
     * @param price            precio del producto
     * @param cant             cant de productos
     * @param taller           nombre del taller al q pertenece
     * @param connectorType    tipo de conector que utiliza
     * @param compatibleDevice dispositivos compatibles
     * @return Charger
     */
    @Override
    public Charger update(List<MultipartFile> files, String name, int price, int cant, String taller, String connectorType, String compatibleDevice, Long id) throws ValueNotFound, BadRequest, IOException {
        Optional<Charger> op = chargerRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }
        Charger charger = chargerRepository.getById(id);
        charger.setName(name);
        charger.setPrice(price);
        charger.setCant(cant);
        charger.setTaller(taller);
        charger.setConnectorType(connectorType);
        charger.setCompatibleDevice(compatibleDevice);

        //Listado de imagenes actual
        List<File> previousImages = charger.getFiles();
        //Comprobando si no son las mismas imagenes
        boolean band = true;
        if (files.size() == charger.getFiles().size()) {
            for (int i = 0; i < files.size() && band; i++) {
                if (!files.get(i).getOriginalFilename().equals(charger.getFiles().get(i).getName())) {
                    band = false;
                }
            }
        } else {
            band = false;
        }

        Charger c = chargerRepository.save(charger);

        if (!band) {
            //Actualizo el movile con la nueva lista de imagenes
            List<File> images = new LinkedList<>();
            for (MultipartFile file : files) {
                File f = new File();
                f.setName(imageService.guardarArchivo(file));
                f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
                f.setProduct(c);
                images.add(fileService.save(f));
            }
            c.setFiles(images);

            //Eliminando todos las imagenes previas vinculadas a este cargador
            for (File file : previousImages) {
                imageService.eliminarImagen(file.getName());
                fileService.deleteById(file.getId());
            }
        }
        return c;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO
     *
     * @param charger
     * @return Charger
     */
    @Override
    public Charger delete(Charger charger) throws ValueNotFound, BadRequest {
        Optional<Charger> op = chargerRepository.findById(charger.getId());
        if (op.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }

        Charger c = chargerRepository.getById(charger.getId());
        for (File file : charger.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        chargerRepository.deleteById(charger.getId());
        return c;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param id identificador del objeto
     * @return Movile
     */
    @Override
    public Charger deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Charger> op = chargerRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }

        Charger charger = chargerRepository.getById(id);
        for (File file : charger.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        chargerRepository.deleteById(id);
        return charger;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param chargers listado de objetos que se quieren eliminar
     * @return List<Movile>  objetos que aun existen en la BD
     */
    @Override
    public List<Charger> deleteAll(List<Charger> chargers) throws ValueNotFound, BadRequest {
        for (Charger charger : chargers)
            for (File file : charger.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        chargerRepository.deleteAll(chargers);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        Query query = em.createQuery("select coalesce(sum(c.cant),0) from Charger c");
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }


}
