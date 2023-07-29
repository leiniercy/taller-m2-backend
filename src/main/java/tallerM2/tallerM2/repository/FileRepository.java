package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tallerM2.tallerM2.model.File;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
    Optional<File> findByNameEquals(String name);
}
