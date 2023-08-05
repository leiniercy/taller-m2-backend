package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Charger;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChargerRepository extends JpaRepository<Charger, Long> {

    List<Charger> findAllByOrderByIdAsc();

    /**
     * Metodo para comprobar que no existen objetos duplicados
     */
    Optional<Charger> findByNameAndPriceAndCantAndConnectorTypeAndCompatibleDevice(
            String name, int price, int cant, String connectorType, String compatibleDevice
    );

}
