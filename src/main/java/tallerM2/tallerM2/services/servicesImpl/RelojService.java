package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.*;
import tallerM2.tallerM2.repository.RelojRepository;
import tallerM2.tallerM2.services.IRelojService;
import tallerM2.tallerM2.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RelojService implements IRelojService {

    @Autowired
    private RelojRepository relojRepository;

    @Autowired
    private ImageService imageService;
    @Autowired
    private FileService fileService;
    @PersistenceContext
    private EntityManager em;

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return una referencia a la entidad que coincida con el identificador.
     */
    @Override
    public Reloj findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Reloj> opt = relojRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ValueNotFound("Reloj not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Reloj>
     */
    @Override
    public List<Reloj> findAll() {
        return relojRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * DEL TALLER 2M
     *
     * @return List<Reloj> listado de objetos del taller 2M
     */
    @Override
    public List<Reloj> findAllTaller2M(){
        return em.createQuery("SELECT r FROM Reloj r WHERE r.taller LIKE 'Taller 2M' ORDER BY r.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * DEL TALLER MJ
     *
     * @return List<Reloj> listado de objetos del taller MJ
     */
    @Override
    public List<Reloj> findAllTallerMJ(){
        return em.createQuery("SELECT r FROM Reloj r WHERE r.taller LIKE 'Taller MJ' ORDER BY r.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Reloj>
     */
    @Override
    public List<Reloj> findAllByOrderByIdAsc() {
        return relojRepository.findAllByOrderByIdAsc();
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
     * @param specialFeature   funcionalidades del reloj
     * @param compatibleDevice dispositivos compatibles
     * @param bateryLife       timepo de uso de la bateria en dias
     * @return Reloj
     */
    @Override
    public Reloj save(List<MultipartFile> files, String name, int price, int cant, String taller,
                      String specialFeature, String compatibleDevice, int bateryLife)
            throws Conflict, BadRequest, IOException {
        List<Reloj> products = em.createQuery("SELECT r FROM Reloj r " +
                        "WHERE r.name LIKE :name AND  r.cant = :cant AND  r.price = :price AND  r.taller = :taller " +
                        "AND  r.specialFeature = :specialFeature " +
                        "AND  r.compatibleDevice = :compatibleDevice " +
                        "AND  r.bateryLife = :bateryLife")
                .setParameter("name", name)
                .setParameter("cant", cant)
                .setParameter("price", price)
                .setParameter("taller", taller)
                .setParameter("specialFeature", specialFeature)
                .setParameter("compatibleDevice", compatibleDevice)
                .setParameter("bateryLife", bateryLife)
                .setMaxResults(1)
                .getResultList();
        if (!products.isEmpty()) {
            throw new Conflict("This reloj is aviable");
        }
        Reloj reloj = new Reloj();
        reloj.setName(name);
        reloj.setPrice(price);
        reloj.setCant(cant);
        reloj.setTaller(taller);
        reloj.setCompatibleDevice(compatibleDevice);
        reloj.setSpecialFeature(specialFeature);
        reloj.setBateryLife(bateryLife);
        Reloj r = relojRepository.save(Util.convertToDto(reloj, Reloj.class));
        //Asignacion de sus imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/reloj/image/" + f.getName());
            f.setProduct(r);
            images.add(fileService.save(f));
        }
        r.setFiles(images);
        return r;
    }

    /**
     * ACUTAULIZAR LA INFORMACION TODA LA INFORMACION DE UN OBJETO
     *
     * @param id               identificador del objeto
     * @param files            listado de imagenes
     * @param name             nombre del producto
     * @param price            precio del producto
     * @param cant             cant de productos
     * @param taller           nombre del taller al q pertenece
     * @param specialFeature   funcionalidades del reloj
     * @param compatibleDevice dispositivos compatibles
     * @param bateryLife       timepo de uso de la bateria en dias
     * @return Reloj
     */
    @Override
    public Reloj update(List<MultipartFile> files, String name, int price, int cant, String taller,
                        String specialFeature, String compatibleDevice, int bateryLife, Long id)
            throws ValueNotFound, BadRequest, IOException {
        Optional<Reloj> op = relojRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Reloj not found");
        }

        Reloj reloj = relojRepository.getById(id);
        reloj.setName(name);
        reloj.setPrice(price);
        reloj.setCant(cant);
        reloj.setTaller(taller);
        reloj.setCompatibleDevice(compatibleDevice);
        reloj.setSpecialFeature(specialFeature);
        reloj.setBateryLife(bateryLife);

        //Eliminando todos las imagenes vinculadas a este cargador
        for (File file : reloj.getFiles()) {
            imageService.eliminarImagen(file.getName());
            fileService.deleteById(file.getId());
        }

        Reloj r = relojRepository.save(Util.convertToDto(reloj, Reloj.class));
        //Actualizo el movile con la nueva lista de imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/reloj/image/" + f.getName());
            f.setProduct(r);
            images.add(fileService.save(f));
        }
        r.setFiles(images);

        return r;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO
     *
     * @param reloj
     * @return Reloj
     */
    @Override
    public Reloj delete(Reloj reloj) throws ValueNotFound, BadRequest {
        Optional<Reloj> op = relojRepository.findById(reloj.getId());
        if (op.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }

        Reloj r = relojRepository.getById(reloj.getId());
        for (File file : reloj.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        relojRepository.deleteById(reloj.getId());
        return r;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param id identificador del objeto
     * @return Reloj
     */
    @Override
    public Reloj deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Reloj> op = relojRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Charger not found");
        }

        Reloj reloj = relojRepository.getById(id);
        for (File file : reloj.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        relojRepository.deleteById(id);
        return reloj;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param relojs listado de objetos que se quieren eliminar
     * @return List<Reloj>  objetos que aun existen en la BD
     */
    @Override
    public List<Reloj> deleteAll(List<Reloj> relojs) throws ValueNotFound, BadRequest {
        for (Reloj reloj : relojs)
            for (File file : reloj.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        relojRepository.deleteAll(relojs);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        Query query = em.createQuery("select coalesce(sum(r.cant),0) from Reloj r");
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }
}
