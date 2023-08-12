package tallerM2.tallerM2.services.servicesImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.model.Sell;
import tallerM2.tallerM2.repository.ProductRepository;
import tallerM2.tallerM2.repository.SellRepository;
import tallerM2.tallerM2.services.ISellService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SellService implements ISellService {

    @Autowired
    SellRepository sellRepository;
    @PersistenceContext
    public EntityManager em;

    @Autowired
    ProductRepository productRepository;


    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Sell
     */
    @Override
    public Sell findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Sell not found");
        }
        return op.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Sell>
     */
    @Override
    public List<Sell> findAll() {
        return sellRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Sell>
     */
    @Override
    public List<Sell> findAllOrderByIdAsc() {
        return em.createQuery("SELECT s FROM Sell s ORDER BY s.id")
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param sell indica el objeto que se desea guradar. No debe ser vacio
     * @return Sell objeto guardado
     */
    @Override
    public Sell save(Sell sell) throws Conflict, BadRequest {
        Optional<Sell> op = sellRepository.findById(sell.getId());
        if (!op.isEmpty()) {
            throw new Conflict("This sell is aviable");
        }
        Sell s = new Sell();
        s.setDescription(sell.getDescription());
        s.setTallerName(sell.getTallerName());
        s.setSellDate(sell.getSellDate());
        s.setCustomer(sell.getCustomer());
        s.setProduct(sell.getProduct());

        return sellRepository.save(s);
    }

    /**
     * METODO PARA ACTUALIZAR TODA LA INFORMACION DE UNA VENTA MEDIANTE SU IDENTIFICADOR
     *
     * @param from informacion actualizada de la venta que se quiere modificar.
     * @param id   identificador de la venta a modificar.
     * @return to  infomracion actualizada de la venta
     */
    @Override
    public Sell update(Sell from, Long id) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(id);
        if (op.isEmpty()) throw new ValueNotFound("Sell not found");
        Sell to = op.get();
        to.setDescription(from.getDescription());
        to.setTallerName(from.getTallerName());
        to.setSellDate(from.getSellDate());
        to.setCustomer(from.getCustomer());
        to.setProduct(from.getProduct());
        return sellRepository.save(to);
    }

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param sell Informacion de la venta que se quiere eliminar. No debe ser vacio
     * @return Sell  toda la informacion del objeto elimnado.
     *
     */
    @Override
    public Sell delete(Sell sell) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(sell.getId());
        if (op.isEmpty()) throw new ValueNotFound("Sell not found");
        sellRepository.delete(sell);
        return op.get();
    }

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Sell  toda la informacion del objeto elimnado.
     *
     */
    @Override
    public Sell deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(id);
        if (op.isEmpty()) throw new ValueNotFound("Sell not found");
        sellRepository.deleteById(id);
        return op.get();
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param sales listado de objetos a eliminar
     * @return List<Sell> listado actualizado con la informacion que queda en la BD
     */
    @Override
    public List<Sell> deleteAll(List<Sell> sales) throws ValueNotFound, BadRequest {
        sellRepository.deleteAll(sales);
        return findAllOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     * */
    @Override
    public long count() {
        return sellRepository.count();
    }
}
