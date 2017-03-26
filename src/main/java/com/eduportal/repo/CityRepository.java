package com.eduportal.repo;

import com.eduportal.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {
	
	City findByName(String cityname);

	City findByCyrname(String cityname);

}
