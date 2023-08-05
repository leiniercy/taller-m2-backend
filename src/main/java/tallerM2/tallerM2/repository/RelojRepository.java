package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Reloj;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelojRepository extends JpaRepository<Reloj, Long> {
    List<Reloj> findAllByOrderByIdAsc();

    /**
     * Metodo para comprobar que no existen objetos duplicados
     */
    Optional<Reloj> findByNameAndPriceAndCantAndSpecialFeatureAndCompatibleDeviceAndBateryLife(
            String name, int price, int cant, String specialFeature, String compatibleDevice, int bateryLife
    );
}
