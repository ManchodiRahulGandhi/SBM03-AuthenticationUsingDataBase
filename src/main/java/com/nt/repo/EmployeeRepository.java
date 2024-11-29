package com.nt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	//Employee findByName(String username);
	Optional<Employee> findByName(String name);
}
