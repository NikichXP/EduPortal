package com.eduportal.repo;

import com.eduportal.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

	@Query( "{'corporation' : ?0}" )
	List<Employee> findByCorporation(String corpName);

	@Query( "{'isAgent': ?0}" )
	List<Employee> findAgents(boolean isAgent);

}
