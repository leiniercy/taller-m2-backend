package tallerM2.tallerM2.services;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Customer;

import java.util.List;

public interface ICustomerService {

    public Customer findById(Long id) throws ValueNotFound, BadRequest;

    public List<Customer> findAll();

    public List<Customer> findAllOrderByIdAsc();

    public Customer save(Customer customer) throws Conflict, BadRequest;

    public Customer update(Customer customer) throws ValueNotFound, BadRequest;

    public Customer delete(Customer customer) throws ValueNotFound, BadRequest;

    public Customer deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Customer> deleteAll(List<Customer> customers)throws ValueNotFound, BadRequest;

    public long count();


}
