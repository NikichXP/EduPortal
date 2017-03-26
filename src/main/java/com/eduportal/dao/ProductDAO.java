package com.eduportal.dao;

import com.eduportal.entity.Product;
import com.eduportal.AppLoader;
import com.eduportal.entity.City;
import com.eduportal.repo.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ProductDAO {

	private static ProductRepository prodRepo;
	private static ProductRepository prodRepo() {
		if (prodRepo == null) {
			prodRepo = AppLoader.get(ProductRepository.class);
		}
		return prodRepo;
	}

	public static boolean create(Product p) {
		prodRepo().save(p);
		return (prodRepo().findOne(p.getId()) != null);
	}

	public static Product get(String id) {
		return prodRepo().findOne(id);
	}

	public static List<Product> getAll() {
		return prodRepo().findAll();
	}

	public static List<Product> getActual(boolean actuality) {
		return prodRepo().listActual(actuality);
	}

	public static List<Product> getAllByCounry(String country) {
		return prodRepo().listByCountry(country);
	}

	public static List<Product> getAllByCity(String city) {
		return prodRepo().listByCity(city);
	}

	public static List<Product> getAllByCity(City city) {
		return prodRepo().listByCity(city.getName());
	}

	public static void save(Product... p) {
		Arrays.asList(p).forEach(prodRepo()::save);
	}

}
