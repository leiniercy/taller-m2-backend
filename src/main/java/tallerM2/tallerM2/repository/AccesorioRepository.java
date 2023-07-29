package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Accesorio;
import tallerM2.tallerM2.model.Product;

import java.util.List;

@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {

    List<Accesorio> findAllByOrderByIdAsc();
    List<Accesorio> findAllByOrderByPriceAsc();
}
