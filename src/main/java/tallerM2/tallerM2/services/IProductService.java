package tallerM2.tallerM2.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.Product;

public interface IProductService {

    public Product findById(Long id) throws ValueNotFound, BadRequest;

    public List<Product> findAll();

    public List<Product> findAllByOrderByIdAsc();

    public Product save(MultipartFile file, String name, int price, int cant) throws Conflict, BadRequest;

    public Product update(MultipartFile newFile, String newName, int newPrice, int newCant, Long id) throws ValueNotFound, BadRequest;

    public Product delete(Product p) throws ValueNotFound, BadRequest;

    public Product deleteById(Long id) throws ValueNotFound, BadRequest;

    public void deleteAll(List<Product> products);

    public long count();

    public Long countByName(String name);
}
