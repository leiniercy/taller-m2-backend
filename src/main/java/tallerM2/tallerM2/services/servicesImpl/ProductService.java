/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.services.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.*;

import tallerM2.tallerM2.services.IProductService;
import tallerM2.tallerM2.utils.Util;
import tallerM2.tallerM2.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Admin
 */
@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    FileService fileService;
    @PersistenceContext
    public EntityManager em;


    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Product
     */
    @Override
    public Product findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Product> opt = productRepository.findById(id);
        if (!opt.isPresent()) {
            throw new ValueNotFound("Product not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<Product>
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<Product>
     */
    @Override
    public List<Product> findAllByOrderByIdAsc() {
        return productRepository.findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE CUYAS CANTIDADES
     * SON MAYORES A 0
     *
     * @return List<Product>
     */
    @Override
    public List<Product> findAllCantThanCero() {
        return em.createQuery("SELECT p FROM Product p WHERE p.cant > 0 ORDER BY p.id")
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param files  listado de imagenes del producto
     * @param name   nombre del producto
     * @param price  precio del producto
     * @param cant   cant de productos
     * @param taller nombre del taller al q pertenece
     * @return Product
     */
    @Override
    public Product save(List<MultipartFile> files, String name, int price, int cant, String taller) throws Conflict, BadRequest, IOException {

        List<Product> products = em.createQuery("SELECT p FROM Product p " +
                        "WHERE p.name LIKE :name " +
                        "AND  p.cant = :cant " +
                        "AND  p.price = :price " +
                        "AND  p.taller = :taller")
                .setParameter("name", name)
                .setParameter("cant", cant)
                .setParameter("price", price)
                .setParameter("taller", taller)
                .setMaxResults(1)
                .getResultList();
        if (!products.isEmpty()) {
            throw new Conflict("This product is aviable");
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCant(cant);
        product.setTaller(taller);

        Product pro = productRepository.save(product);
        //Asignacion de sus imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
            f.setProduct(pro);
            images.add(fileService.save(f));
        }
        pro.setFiles(images);
        return pro;

    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO EXISTA, SE MAPEA SU NUEVA INFORMACION,
     * Y LUEGO SE GURADA LA INFORMACION
     *
     * @param files  listado de imagenes del producto
     * @param name   nombre del producto
     * @param price  precio del producto
     * @param cant   cant de productos
     * @param taller nombre del taller al q pertenece
     * @param id     cant del producto a modificar
     * @return Product
     */
    @Override
    public Product update(List<MultipartFile> files, String name, int price, int cant, String taller, Long id) throws ValueNotFound, BadRequest, IOException {
        Optional<Product> op = productRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Product not found");
        }
        Product product = productRepository.getById(id);
        product.setName(name);
        product.setPrice(price);
        product.setCant(cant);
        product.setTaller(taller);

        //Eliminando todos las imagenes vinculadas a este cargador
        for (File file : product.getFiles()) {
            imageService.eliminarImagen(file.getName());
            fileService.deleteById(file.getId());
        }

        Product pro = productRepository.save(product);

        //Actualizo el movile con la nueva lista de imagenes
        List<File> images = new LinkedList<>();
        for (MultipartFile file : files) {
            File f = new File();
            f.setName(imageService.guardarArchivo(file));
            f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
            f.setProduct(pro);
            images.add(fileService.save(f));
        }
        pro.setFiles(images);

        return pro;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param a que se quiere eliminar
     * @return Producto
     */
    @Override
    public Product delete(Product a) throws ValueNotFound, BadRequest {
        Optional<Product> op = productRepository.findById(a.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Product not found");
        }

        Product product = productRepository.getById(a.getId());
        for (File file : product.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        productRepository.delete(a);
        return product;
    }

    /**
     * METODO PARA ELIMINAR UN ACCESORIO.
     *
     * @param id
     * @return Product
     */
    @Override
    public Product deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Product> op = productRepository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Product not found");
        }

        Product product = productRepository.getById(id);
        for (File file : product.getFiles()) {
            imageService.eliminarImagen(file.getName());
        }
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public List<Product> deleteAll(List<Product> products) throws ValueNotFound, BadRequest {
        for (Product product : products)
            for (File file : product.getFiles()) {
                imageService.eliminarImagen(file.getName());
            }
        productRepository.deleteAll(products);
        return findAllByOrderByIdAsc();
    }

    /**
     * METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE
     *
     * @return long
     */
    @Override
    public long count() {
        long total = 0;
        List<Product> products = findAll().stream()
                .filter(product -> !(product instanceof Movile) && !(product instanceof Reloj) && !(product instanceof Charger))
                .collect(Collectors.toList());
        if (products.isEmpty()) return 0;
        for (Product product : products) {
            total += product.getCant();
        }
        return total;
    }
}
