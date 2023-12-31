package tallerM2.tallerM2.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Product;

public interface IProductService {

    public Product findById(Long id) throws ValueNotFound, BadRequest;

    public List<Product> findAll(String taller);

    public List<Product> findAllAccesorios(String taller);

    public List<Product> findAllByOrderByIdAsc();

    public List<Product> findAllCantThanCero(String taller);

    public Product save(List<MultipartFile> files, String name, int price, int cant, String taller) throws Conflict, BadRequest, IOException;

    public Product update(List<MultipartFile> files, String name, int price, int cant, String taller, Long id) throws ValueNotFound, BadRequest, IOException, Conflict;

    public Product delete(Product p) throws ValueNotFound, BadRequest;

    public Product deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<Product> deleteAll(List<Product> products) throws ValueNotFound, BadRequest;

    public long count(String taller);
}
