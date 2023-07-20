package tallerM2.tallerM2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tallerM2.tallerM2.model.Movile;

@Repository
public interface MovileRepository extends JpaRepository<Movile, Long> {

    // Find all Moviles for id in order asc
    List<Movile> findAllByOrderByIdAsc();

    // Find all Moviles for price in order asc
//    List<Movile> findAllByOrderByPriceAsc();

//    //Find greater than id
    List<Movile> findByIdGreaterThan(long id);

    @Query(value = "Select * from Movile LIKE '%name%';", nativeQuery = true)
    Long countByName(String name);

    @Query(value = "Select m from Movile m where m.price < :maxPrice", nativeQuery = true)
    List<Movile> findByPriceLessThan(float maxPrice);
}
