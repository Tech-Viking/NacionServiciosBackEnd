package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
