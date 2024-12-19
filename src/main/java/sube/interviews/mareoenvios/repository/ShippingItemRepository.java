package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.ShippingItem;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingItemRepository extends JpaRepository<ShippingItem, Integer> {

  @Query(value = "SELECT si.product_id, p.description, SUM(si.product_count) AS total_count " +
            "FROM Shipping_item si " +
            "JOIN Product p ON si.product_id = p.id " +
            "GROUP BY si.product_id, p.description " +
            "ORDER BY total_count DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3Products();
    
}