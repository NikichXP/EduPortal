package com.eduportal.repo;

import com.eduportal.entity.AuthSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AuthRepository extends MongoRepository<AuthSession, String> {

	@Query("{'timeout' : {$lt : ?0}}")
	List<AuthSession> getOutdated(long timestamp);

}
