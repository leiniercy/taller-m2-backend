package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.Sell;

@Repository
public interface SellRepository extends JpaRepository<Sell,Long> {

}
