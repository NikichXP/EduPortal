package com.eduportal.repo;

import com.eduportal.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

	@Query("{$and : [{'clientId' : ?0}, {'createdById' : ?0}, {'curatorId' : ?0}, {'agentId' : ?0}]}")
	List<Order> findMyOrders(String userid);

	@Query("{'clientId' : ?0}")
	List<Order> findByClient(String userid);

	@Query("{'createdById' : ?0}")
	List<Order> findByCreator(String userid);

	@Query("{'curatorId' : ?0}")
	List<Order> findByCurator(String userid);

	@Query("{'agentId' : ?0}")
	List<Order> findByAgent(String userid);

	@Query("{'productId' : ?0}")
	List<Order> findByProductId(String prod);

	@Query("{?0 : ?1}")
	public List<Order> listCustom1ArgQuery(String arg1, Object arg2);

	@Query("{?0 : ?1, ?2 : ?3}")
	public List<Order> listCustom2ArgQuery(String key0, Object value0,
	                                       String key1, Object value1);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5}")
	public List<Order> listCustom3ArgQuery(String key0, Object value0,
	                                       String key1, Object value1,
	                                       String key2, Object value2);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5, ?6 : ?7}")
	public List<Order> listCustom4ArgQuery(String key0, Object value0,
	                                       String key1, Object value1,
	                                       String key2, Object value2,
	                                       String key3, Object value3);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5, ?6 : ?7, ?8 : ?9}")
	public List<Order> listCustom5ArgQuery(String key0, Object value0,
	                                       String key1, Object value1,
	                                       String key2, Object value2,
	                                       String key4, Object value4,
	                                       String key3, Object value3);
}
