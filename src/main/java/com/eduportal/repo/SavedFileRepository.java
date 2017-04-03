package com.eduportal.repo;

import com.eduportal.entity.SavedFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SavedFileRepository extends MongoRepository<SavedFile, String> {
}
