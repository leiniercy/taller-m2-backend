/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Product;

/**
 *
 * @author Admin
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "Select * from Product LIKE '%name%';", nativeQuery = true)
    Long countByName(String name);

    // Find all Products for id in order asc
    List<Product> findAllByOrderByIdAsc();

    // Find all Products for price in order asc
    List<Product> findAllByOrderByPriceAsc();
}
