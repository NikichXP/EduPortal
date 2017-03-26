package com.eduportal.repo;

import com.eduportal.entity.ClientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClientRepository extends MongoRepository<ClientEntity, String> {

	@Query( "{'creator' : ?0}" )
	List<ClientEntity> findCreatedBy(String id);

}
