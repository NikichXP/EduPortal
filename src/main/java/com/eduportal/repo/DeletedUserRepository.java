package com.eduportal.repo;

import com.eduportal.entity.DeletedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeletedUserRepository extends MongoRepository <DeletedUser, String> {
}
