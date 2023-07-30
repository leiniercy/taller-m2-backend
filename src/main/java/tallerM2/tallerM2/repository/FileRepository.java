package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.File;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
    Optional<File> findByNameEquals(String name);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM File f WHERE f.id = ?1",
            nativeQuery = true)
    void eliminarPorId( Long id);

}
