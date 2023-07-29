package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {

    Optional<Accesorio> findByNameAndCantAndPrice(String name, int cant, int price);

    List<Accesorio> findAllByOrderByIdAsc();
    List<Accesorio> findAllByOrderByPriceAsc();
}
