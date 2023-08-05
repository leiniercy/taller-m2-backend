package tallerM2.tallerM2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tallerM2.tallerM2.model.Movile;

@Repository
public interface MovileRepository extends JpaRepository<Movile, Long> {

    /**
    * Metodo para comprobar que no existen objetos duplicados
    * */
    Optional<Movile> findByNameAndPriceAndCantAndSizeStorageAndRamAndCamaraTraseraAndCamaraFrontalAndBanda2GAndBanda3GAndBanda4GAndBanda5GAndBateria(
            String name, int price, int cant, int sizeStorage, int ram, int camaraTrasera, int camaraFrontal,
            boolean banda2G, boolean banda3G, boolean banda4G, boolean banda5G, long bateria
    );

    /**
     * Metodo para ordenar todos los moviles por id
     * */
    List<Movile> findAllByOrderByIdAsc();

    @Query(value = "Select * from Movile LIKE '%name%';", nativeQuery = true)
    Long countByName(String name);

    @Query(value = "Select m from Movile m where m.price < :maxPrice", nativeQuery = true)
    List<Movile> findByPriceLessThan(float maxPrice);
}
