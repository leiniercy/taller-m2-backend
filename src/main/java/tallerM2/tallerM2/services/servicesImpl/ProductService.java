/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.services.servicesImpl;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;

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
    private ProductRepository repository;

    @Autowired
    private ImageService imageService;

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return una referencia a la entidad que coincida con el identificador.
     *
     */
    @Override
    public Product findById(Long id) throws ValueNotFound, BadRequest {
        Optional<Product> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ValueNotFound("Product not found");
        }
        return opt.get();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return un listado de objetos
     *
     */
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return un listado de objetos
     *
     */
    @Override
    public List<Product> findAllByOrderByIdAsc() {
        List<Product> list = findAll();
        Collections.sort(list);
        return list;
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param file imagen del producto
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @return el objeto guardado
     *
     */
    @Override
    public Product save(Product p) throws Conflict, BadRequest {
        //Comprpbando si el objeto no exsite    
        BinarySerch(findAllByOrderByIdAsc(), p.getName());

        return repository.save(Util.convertToDto(p, Product.class));
    }

    /**
     * Busqueda binaria que se encarga de verificar si existe algun objeto con
     * el mismo nombre primero se convierte toda la palabra a minuscula y luego
     * se comprueba
     *
     * @param List<Object> , String
     * @return void
     */
    private void BinarySerch(List<Product> list, String nombre) throws Conflict {
        int inicio = 0;
        int fin = list.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            String nombreMedio = list.get(medio).getName().toLowerCase();
            if (nombreMedio.equals(nombre.toLowerCase())) {
                throw new Conflict("This product already exists");
                // return true; Concidencia encontrada
            } else if (nombreMedio.compareTo(nombre.toLowerCase()) < 0) {
                inicio = medio + 1; // Buscar en la mitad derecha
            } else {
                fin = medio - 1; // Buscar en la mitad izquierda
            }
        }

        // return false; No se encontro ninguna coincidencia
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO EXISTA, SE MAPEA SU NUEVA INFORMACION,
     * Y LUEGO SE GURADA LA INFORMACION
     *
     * @param file imagen del producto
     * @param name nombre del producto
     * @param price precio del producto
     * @param cant cant de productos
     * @param id cant del producto a modificar
     * @return to
     *
     */
    @Override
    public Product update(Product from, Long id)
            throws ValueNotFound, BadRequest {

        Optional<Product> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Product not found");
        }
        Product to = new Product();
        to.setId(id);
        to.setName(from.getName());
        to.setPrice(from.getPrice());
        to.setCant(from.getCant());
        if(!from.getImage().equals("emptyFile.png")){
          to.setImage(from.getImage());     
        }else{
          to.setImage(op.get().getImage());
        }
       
        return repository.save(to);
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param Producto que se quiere eliminar
     * @return Producto eliminado
     *
     */
    @Override
    public Product delete(Product p) throws ValueNotFound, BadRequest {
        Optional<Product> op = repository.findById(p.getId());
        if (!op.isPresent()) {
            throw new ValueNotFound("Product not found");
        }

        Product product = repository.getById(p.getId());
        repository.delete(p);
        return product;
    }

    /**
     * METODO PARA ELIMINAR UN OBJETO POR SU IDENTIFICADOR
     *
     * @param Producto que se quiere eliminar
     * @return Producto eliminado
     *
     */
    @Override
    public Product deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<Product> op = repository.findById(id);
        if (!op.isPresent()) {
            throw new ValueNotFound("Product not found");
        }

        Product movile = repository.getById(id);
        repository.deleteById(id);
        return movile;
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param List<Object>
     * @return List<Object> ordenados asendente por ID
     *
     */
    @Override
    public List<Product> deleteAll(List<Product> products)throws ValueNotFound, BadRequest{
        repository.deleteAll(products);
        return findAllByOrderByIdAsc();
    }
    // METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE

    @Override
    public long count() {
        return repository.count();
    }
    // METODO QUE DEVUELVE LA CANTIDAD DE OBJETOS QUE EXISTE DADO UN NOMBRE

    @Override
    public Long countByName(String name) {
        return repository.countByName(name);
    }

}
