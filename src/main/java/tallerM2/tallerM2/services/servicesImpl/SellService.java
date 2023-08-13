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
import tallerM2.tallerM2.utils.Util;
import tallerM2.tallerM2.utils.dto.SellRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Sell>
     */
    @Override
    public List<Sell> findAllByDate(LocalDate sellDate) {
        return em.createQuery("SELECT s FROM Sell s WHERE s.sellDate = :fecha ORDER BY s.id")
                .setParameter("fecha", sellDate)
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param sellRequest contiene la informacion de los objetos que se quieren vender
     * @return List<Sell>  con los objetos vendidos
     */
    @Override
    public List<Sell> save(SellRequest sellRequest) throws Conflict, BadRequest {

//        Optional<Sell> op = sellRepository.findById(sell.getId());
//        if (!op.isEmpty()) {
//            throw new Conflict("This sell is aviable");
//        }

        //Realizo las ventas de los objetos
        List<Sell> sales = new LinkedList<>();
        Random random = new Random();
        for(int i=0; i < sellRequest.getProducts().size(); i++){
            Sell sell = new Sell();
            sell.setId(random.nextLong());
            sell.setTallerName(sellRequest.getTallerName());
            sell.setSellDate(sellRequest.getDate());
            sell.setCustomer(sellRequest.getCustomer());
            sell.setDescription( sellRequest.getDescriptions().get(i));
            sell.setCantProduct(sellRequest.getQuantities().get(i));
            sell.setSalePrice(sellRequest.getPrices().get(i) * sell.getCantProduct());
            sell.setProduct(sellRequest.getProducts().get(i));
            sales.add(sellRepository.save(sell));
        }

        //Actualizo la informacion de los productos vendidos
        for(Sell s : sales){
            Product product = s.getProduct();
            product.setId(s.getProduct().getId());
            product.setName(s.getProduct().getName());
            product.setPrice(s.getProduct().getPrice());
            product.setFiles(s.getProduct().getFiles());
            //actualizo la cantidad real de productos restandole la cantidad vendida
            product.setCant(s.getProduct().getCant() - s.getCantProduct());
            s.setProduct(productRepository.save(product));
        }

        return sales;
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
        to.setCantProduct(from.getCantProduct());
        to.setSalePrice(from.getSalePrice());
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
     */
    @Override
    public Sell delete(Sell sell) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(sell.getId());
        if (op.isEmpty()) throw new ValueNotFound("Sell not found");
        sellRepository.delete(sell);
        Product product = sell.getProduct();
        product.setId(sell.getProduct().getId());
        product.setName(sell.getProduct().getName());
        product.setPrice(sell.getProduct().getPrice());
        product.setFiles(sell.getProduct().getFiles());
        //actualizo la cantidad real de productos sumandole la cantidad vendida
        product.setCant(sell.getProduct().getCant() + sell.getCantProduct());
        sell.setProduct(productRepository.save(product));
        return op.get();
    }

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Sell  toda la informacion del objeto elimnado.
     */
    @Override
    public Sell deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Sell> op = sellRepository.findById(id);
        if (op.isEmpty()) throw new ValueNotFound("Sell not found");
        Sell s = op.get();
        sellRepository.deleteById(id);
        Product product = s.getProduct();
        product.setId(s.getProduct().getId());
        product.setName(s.getProduct().getName());
        product.setPrice(s.getProduct().getPrice());
        product.setFiles(s.getProduct().getFiles());
        //actualizo la cantidad real de productos sumandole la cantidad vendida
        product.setCant(s.getProduct().getCant() + s.getCantProduct());
        s.setProduct(productRepository.save(product));
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
        for(Sell s : sales){
            Product product = s.getProduct();
            product.setId(s.getProduct().getId());
            product.setName(s.getProduct().getName());
            product.setPrice(s.getProduct().getPrice());
            product.setFiles(s.getProduct().getFiles());
            //actualizo la cantidad real de productos sumandole la cantidad vendida
            product.setCant(s.getProduct().getCant() + s.getCantProduct());
            s.setProduct(productRepository.save(product));
        }
        return findAllOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        return sellRepository.count();
    }
}
