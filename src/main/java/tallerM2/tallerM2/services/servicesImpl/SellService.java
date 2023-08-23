package tallerM2.tallerM2.services.servicesImpl;


import java.time.DayOfWeek;
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
import tallerM2.tallerM2.utils.dto.SellMonthRequest;
import tallerM2.tallerM2.utils.dto.SellRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
    public List<Sell> findAllByDate(LocalDate sellDate, String taller) {
        return em.createQuery("SELECT s FROM Sell s WHERE s.sellDate = :fecha " +
                        "AND s.product.taller LIKE :taller " +
                        "ORDER BY s.id")
                .setParameter("fecha", sellDate)
                .setParameter("taller", taller)
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
        for (int i = 0; i < sellRequest.getProducts().size(); i++) {
            Sell sell = new Sell();
            sell.setId(random.nextLong());
            sell.setTallerName(sellRequest.getTallerName());
            sell.setSellDate(sellRequest.getDate());
            sell.setCustomer(sellRequest.getCustomer());
            sell.setDescription(sellRequest.getDescriptions().get(i));
            sell.setCantProduct(sellRequest.getQuantities().get(i));
            sell.setSalePrice(sellRequest.getPrices().get(i) * sell.getCantProduct());
            sell.setProduct(sellRequest.getProducts().get(i));
            sales.add(sellRepository.save(sell));
        }

        //Actualizo la informacion de los productos vendidos
        for (Sell s : sales) {
            Product product = s.getProduct();
            product.setId(s.getProduct().getId());
            product.setName(s.getProduct().getName());
            product.setPrice(s.getProduct().getPrice());
            product.setTaller(s.getProduct().getTaller());
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
        for (Sell s : sales) {
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

    /**
     * METODO QUE DEVUELVE EL TOTAL RECAUDADO POR OBJETOS VENDIDOS EL AÑO
     * ACTUAL EN UN MES ESPECIFICADO
     *
     * @return long
     */
    public Long countSellByMonth(int month) {
        LocalDate date = LocalDate.now();
        Query query = em.createQuery("select coalesce(sum(s.salePrice),0) from Sell s " +
                        "WHERE EXTRACT( year FROM s.sellDate ) = :year " +
                        "AND EXTRACT(MONTH FROM s.sellDate ) = :month")
                .setParameter("year", date.getYear())
                .setParameter("month", month);
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS VENDIDOS EL AÑO
     * ACTUAL EN UN MES Y  UN PRODUCTO ESPECIFICADO
     *
     * @return long
     */
    public Long countSellByMonthAndAccesorio(int month) {
        LocalDate date = LocalDate.now();
        Query query = em.createQuery("SELECT COALESCE(SUM(s.cantProduct),0) FROM Sell s " +
                        "JOIN Product p ON s.product.id = p.id " +
                        "WHERE EXTRACT( year FROM s.sellDate ) = :year " +
                        "AND EXTRACT(MONTH FROM s.sellDate ) = :month " +
                        "AND p.id NOT IN (SELECT c.id FROM Charger c) " +
                        "AND p.id NOT IN (SELECT m.id FROM Movile m) "+
                        "AND p.id NOT IN (SELECT r.id FROM Reloj r) ")
                .setParameter("year", date.getYear())
                .setParameter("month", month);

        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS VENDIDOS EL AÑO
     * ACTUAL EN UN MES Y  UN PRODUCTO ESPECIFICADO
     *
     * @return long
     */
    public Long countSellByMonthAndCharger(int month) {
        LocalDate date = LocalDate.now();
        Query query = em.createQuery("select coalesce(sum(s.cantProduct),0) from Sell s " +
                        "JOIN Charger c ON s.product.id = c.id " +
                        "WHERE EXTRACT( year FROM s.sellDate ) = :year " +
                        "AND EXTRACT(MONTH FROM s.sellDate ) = :month ")
                .setParameter("year", date.getYear())
                .setParameter("month", month);

        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS VENDIDOS EL AÑO
     * ACTUAL EN UN MES Y  UN PRODUCTO ESPECIFICADO
     *
     * @return long
     */
    public Long countSellByMonthAndMovile(int month) {
        LocalDate date = LocalDate.now();
        Query query = em.createQuery("select coalesce(sum(s.cantProduct),0) from Sell s " +
                        "JOIN Movile m ON s.product.id = m.id " +
                        "WHERE EXTRACT( year FROM s.sellDate ) = :year " +
                        "AND EXTRACT(MONTH FROM s.sellDate ) = :month ")
                .setParameter("year", date.getYear())
                .setParameter("month", month);

        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS VENDIDOS EL AÑO
     * ACTUAL EN UN MES Y  UN PRODUCTO ESPECIFICADO
     *
     * @return long
     */
    public Long countSellByMonthAndReloj(int month) {
        LocalDate date = LocalDate.now();
        Query query = em.createQuery("select coalesce(sum(s.cantProduct),0) from Sell s " +
                        "JOIN Reloj r ON s.product.id = r.id " +
                        "WHERE EXTRACT( year FROM s.sellDate ) = :year " +
                        "AND EXTRACT(MONTH FROM s.sellDate ) = :month ")
                .setParameter("year", date.getYear())
                .setParameter("month", month);

        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }
   
    
    /**
     * METODO QUE DEVUELVE UNA LISTA CON EL TOTAL RECAUDADO POR OBJETOS VENDIDOS
     * POR CADA DIA DE LA SEMANA ACTUAL
     *
     * @return long
     */
    public List<Long> getSalesByCurrentWeek(){
        
        List<Long> list = new LinkedList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayofWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayofWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        
        LocalDate day  = firstDayofWeek;
        while (!day.isAfter(lastDayofWeek)) {
            list.add(countSellByDayOfWeek(day));
            day = day.plusDays(1);
        }
        
        
        return list;
    }
    
     /**
     * METODO QUE DEVUELVE EL TOTAL RECAUDADO POR OBJETOS VENDIDOS EN UN DIA DE LA SEMANA
     * ESPECIFICADO PREVIAMENTE
     *
      * @param date
     * @return long
     */
    public Long countSellByDayOfWeek(LocalDate date) {
        Query query = em.createQuery("SELECT COALESCE(SUM(s.salePrice),0) FROM Sell s " +
                        "JOIN Product p ON s.product.id = p.id " +
                        "WHERE s.sellDate = :date ")
                .setParameter("date", date);
       
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }
    

}

