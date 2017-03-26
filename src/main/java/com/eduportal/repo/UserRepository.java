package com.eduportal.repo;

import com.eduportal.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, String> {

	@Query("{'mail' : ?0}")
	UserEntity findByMail(String mail);

	@Query("{'phone' : ?0}")
	UserEntity findByPhone(String phone);

	@Query("{'isActive' : ?0}")
	List<UserEntity> findUnactiveUsers(boolean active);

	@Query("{$or: [{'phone' : ?0}, {'mail' : ?1}]}")
	UserEntity findByPhoneOrMail(String phone, String mail);

}
