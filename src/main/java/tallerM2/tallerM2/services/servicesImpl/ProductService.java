/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.services.servicesImpl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import javax.persistence.Query;

import tallerM2.tallerM2.utils.MinioAdapter;

/**
 * @author Admin
 */
@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    FileService fileService;
    @Autowired
    MinioAdapter minioAdapter;
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
     * @return List<Product> devuelve un listado general de todos los productos
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE ORDENADOS POR NOMBRE
     *
     * @return List<Product> devuelve un listado de productos que no son reloj,
     * movil, cargador
     */
    @Override
    public List<Product> findAllAccesorios() {
        return em.createQuery("SELECT p FROM Product p WHERE p.id NOT IN (SELECT c.id FROM Charger c) "
                + "AND p.id NOT IN (SELECT m.id FROM Movile m) "
                + "AND p.id NOT IN (SELECT r.id FROM Reloj r) "
                + "ORDER BY p.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER 2M
     *
     * @return List<Product> listado de productos del taller 2M
     */
    public List<Product> findAllProductsTaller2M() {
        return em.createQuery("SELECT p FROM Product p WHERE p.taller LIKE 'Taller 2M' ORDER BY p.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER MJ
     *
     * @return List<Product> listado de productos del taller MJ
     */
    public List<Product> findAllProductsTallerMJ() {
        return em.createQuery("SELECT p FROM Product p WHERE p.taller LIKE 'Taller MJ' ORDER BY p.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER 2M
     *
     * @return List<Product> listado de accesorios del taller 2M
     */
    public List<Product> findAllAccesoriosTaller2M() {
        return em.createQuery("SELECT p FROM Product p WHERE p.id NOT IN (SELECT c.id FROM Charger c) "
                + "AND p.id NOT IN (SELECT m.id FROM Movile m) "
                + "AND p.id NOT IN (SELECT r.id FROM Reloj r) "
                + "AND p.taller LIKE 'Taller 2M' ORDER BY p.name")
                .getResultList();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO DEL
     * TALLER MJ
     *
     * @return List<Product> listado de accesorios del taller MJ
     */
    public List<Product> findAllAccesoriosTallerMJ() {
        return em.createQuery("SELECT p FROM Product p WHERE p.id NOT IN (SELECT c.id FROM Charger c) "
                + "AND p.id NOT IN (SELECT m.id FROM Movile m) "
                + "AND p.id NOT IN (SELECT r.id FROM Reloj r) "
                + "AND p.taller LIKE 'Taller MJ' ORDER BY p.name")
                .getResultList();
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
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE CUYAS CANTIDADES SON
     * MAYORES A 0
     *
     * @return List<Product>
     */
    @Override
    public List<Product> findAllCantThanCero(String taller) {
        return em.createQuery("SELECT p FROM Product p WHERE p.cant > 0 AND p.taller LIKE :taller ORDER BY p.id")
                .setParameter("taller", taller)
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param files listado de imagenes del producto
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @param taller nombre del taller al q pertenece
     * @return Product
     */
    @Override
    public Product save(List<MultipartFile> files, String name, int price, int cant, String taller) throws Conflict, BadRequest, IOException {

        List<Product> products = em.createQuery("SELECT p FROM Product p "
                + "WHERE p.name LIKE :name "
                + "AND  p.cant = :cant "
                + "AND  p.price = :price "
                + "AND  p.taller = :taller")
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
                String nombreImagen = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                minioAdapter.uploadFile(nombreImagen, file.getInputStream(),file.getSize());
                f.setName(nombreImagen);
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
     * @param files listado de imagenes del producto
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @param taller nombre del taller al q pertenece
     * @param id cant del producto a modificar
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

        //Listado de imagenes actual
        List<File> previousImages = product.getFiles();
        //Comprobando si no son las mismas imagenes
        boolean band = true;
        if (files.size() == product.getFiles().size()) {
            for (int i = 0; i < files.size() && band; i++) {
                if (!files.get(i).getOriginalFilename().equals(product.getFiles().get(i).getName())) {
                    band = false;
                }
            }
        } else {
            band = false;
        }

        Product pro = productRepository.save(product);
        if (!band) {
            //Actualizo el movile con la nueva lista de imagenes
            List<File> images = new LinkedList<>();
            for (MultipartFile file : files) {
                File f = new File();
                String nombreImagen = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                minioAdapter.uploadFile(nombreImagen, file.getInputStream(),file.getSize());
                f.setName(nombreImagen);
                f.setUrl("http://localhost:8080/api/v1/product/image/" + f.getName());
                f.setProduct(pro);
                images.add(fileService.save(f));
            }
            pro.setFiles(images);

            //Eliminando todos las imagenes anteriores vinculadas a este accesorio
            for (File file : previousImages) {
                minioAdapter.deleteFile(file.getName());
                fileService.deleteById(file.getId());
            }

        }

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
            minioAdapter.deleteFile(file.getName());
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
            minioAdapter.deleteFile(file.getName());
        }
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public List<Product> deleteAll(List<Product> products) throws ValueNotFound, BadRequest {
        for (Product product : products) {
            for (File file : product.getFiles()) {
                minioAdapter.deleteFile(file.getName());
            }
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
    public long count(String taller) {
        Query query = em.createQuery("select coalesce(sum(p.cant),0) from Product p "
                        + "WHERE p.taller = :taller "
                        +"AND p.id NOT IN (SELECT c.id FROM Charger c) "
                        +"AND p.id NOT IN (SELECT m.id FROM Movile m) "
                        +"AND p.id NOT IN (SELECT r.id FROM Reloj r)")
                .setParameter("taller", taller);
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }
}
