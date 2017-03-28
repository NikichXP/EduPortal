package com.eduportal.repo;

import com.eduportal.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserActionRepository extends MongoRepository<UserAction, String> {
}
