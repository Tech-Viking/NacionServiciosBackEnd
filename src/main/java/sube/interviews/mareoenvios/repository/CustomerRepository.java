package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}