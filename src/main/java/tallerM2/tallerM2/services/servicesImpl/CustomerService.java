package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Customer;
import tallerM2.tallerM2.repository.CustomerRepository;
import tallerM2.tallerM2.services.ICustomerService;
import tallerM2.tallerM2.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @PersistenceContext
    public EntityManager em;

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Customer
     */
    @Override
    public Customer findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Customer> op = customerRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Customer not found");
        }
        return op.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Customer>
     */
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Customer>
     */
    @Override
    public List<Customer> findAllOrderByIdAsc() {
        return em.createQuery("SELECT c FROM Customer c ORDER BY c.id")
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param customer informacion del cliente
     * @return Customer
     */
    @Override
    public Customer save(Customer customer) throws Conflict, BadRequest {
        List<Customer> customers = em.createQuery("SELECT c FROM Customer c " +
                        "WHERE c.customerName LIKE :name " +
                        "AND  c.customerMovile LIKE :movile")
                .setParameter("name", customer.getCustomerName())
                .setParameter("movile", customer.getCustomerMovile())
                .setMaxResults(1)
                .getResultList();
        if (!customers.isEmpty()) {
            throw new Conflict("This customer is aviable");
        }
        return customerRepository.save(Util.convertToDto(customer, Customer.class));
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO EXISTA, SE MAPEA SU NUEVA INFORMACION,
     * Y LUEGO SE GURADA LA INFORMACION
     *
     * @param from informacion del cliente a modificar
     * @param id   identificador del cliente a modificar
     * @return Product
     */
    @Override
    public Customer update(Customer from) throws ValueNotFound, BadRequest {
        Optional<Customer> op = customerRepository.findById(from.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Customer not found");
        }
        Customer to = op.get();
        to.setId(from.getId());
        to.setCustomerName(from.getCustomerName());
        to.setCustomerMovile(from.getCustomerMovile());
        return customerRepository.save(Util.convertToDto(to, Customer.class));
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param customer  cliente que se quiere eliminar
     * @return Customer
     */
    @Override
    public Customer delete(Customer customer) throws ValueNotFound, BadRequest {
        Optional<Customer> op = customerRepository.findById(customer.getId());
        if (op.isEmpty()) {
            throw new ValueNotFound("Customer not found");
        }
        customerRepository.delete(customer);
        return op.get();
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO.
     *
     * @param id identificador del objeto a eliminar
     * @return Customer
     */
    @Override
    public Customer deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Customer> op = customerRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Customer not found");
        }
        customerRepository.deleteById(id);
        return op.get();
    }
    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param customers listado de objetos que se quieren eliminar
     * @return List<Customer>  objetos que aun existen en la BD
     */
    @Override
    public List<Customer> deleteAll(List<Customer> customers) throws ValueNotFound, BadRequest {
        customerRepository.deleteAll(customers);
        return findAllOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        return customerRepository.count();
    }
}
