package sube.interviews.mareoenvios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
	
    @Query("SELECT s FROM Shipping s LEFT JOIN FETCH s.shippingItems WHERE s.id = :shippingId")
  Optional<Shipping> findByIdWithItems(Integer shippingId);
    
}