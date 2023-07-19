package tallerM2.tallerM2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tallerM2.tallerM2.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    
}
