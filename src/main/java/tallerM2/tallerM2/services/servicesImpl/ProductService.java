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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.File;

import tallerM2.tallerM2.model.Product;
import tallerM2.tallerM2.services.IProductService;
import tallerM2.tallerM2.utils.Util;
import tallerM2.tallerM2.repository.ProductRepository;

/**
 *
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
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param files listado de imagenes del producto
     * @param name  nombre del producto
     * @param price precio del producto
     * @param cant  cant de productos
     * @return Product
     */
    @Override
    public Product save(List<MultipartFile> files, String name, int price, int cant) throws Conflict, BadRequest, IOException {

        Optional<Product> op = productRepository.findByNameAndCantAndPrice(name, price, cant);
        if (op.isPresent()) {
            throw new Conflict("This product is aviable");
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCant(cant);

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
     * @param files listado de imagenes del producto
     * @param name  nombre del producto
     * @param price precio del producto
     * @param cant  cant de productos
     * @param id    cant del producto a modificar
     * @return Product
     */
    @Override
    public Product update(List<MultipartFile> files, String name, int price, int cant, Long id) throws ValueNotFound, BadRequest, IOException {
        Optional<Product> op = productRepository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("Product not found");
        }
        Product product = productRepository.getById(id);
        product.setName(name);
        product.setPrice(price);
        product.setCant(cant);


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
        return productRepository.count();
    }
}
