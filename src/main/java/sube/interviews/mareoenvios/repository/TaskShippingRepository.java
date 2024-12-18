package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.TaskShipping;

@Repository
public interface TaskShippingRepository extends JpaRepository<TaskShipping, Integer> {
}
