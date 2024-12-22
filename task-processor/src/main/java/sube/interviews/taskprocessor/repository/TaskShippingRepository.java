package sube.interviews.taskprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sube.interviews.taskprocessor.entity.TaskShipping;

@Repository
public interface TaskShippingRepository extends JpaRepository<TaskShipping, Integer> {
}
