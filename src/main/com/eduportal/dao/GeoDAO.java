package com.eduportal.dao;

import com.eduportal.AppLoader;
import com.eduportal.entity.City;
import com.eduportal.repo.CityRepository;

import java.util.List;

public class GeoDAO {

	private static CityRepository repo;

	private static CityRepository repo() {
		if (repo == null) {
			repo = AppLoader.get(CityRepository.class);
		}
		return repo;
	}

	public static City createCity(String name, String country) {
		City city = new City();
		city.setName(name);
		city.setCountry(country);
		repo().save(city);
		return city;
	}

	public static void saveCity(City c) {
		repo().save(c);
	}

	public static City getCity(String cityname) {
		try {
			City c = repo().findByName(cityname);
			if (c == null) {
				c = repo().findByCyrname(cityname);
			}
			return c;
		} catch (Exception e) {
			return null;
		}
	}

	public static City getCityById(String id) {
		return repo().findOne(id);
	}

	public static List<String> getCountryList(String filterExp) {
		throw new UnsupportedClassVersionError("Not yet done");
	}

	public static String getCountry(String country) {
		throw new UnsupportedClassVersionError("Not yet done");
	}

	public static List<City> getCityList() {
		return repo().findAll();
	}

	public static boolean deleteCity(String cityName) {
		if (ProductDAO.getAllByCity(cityName).size() != 0) {
			return false;
		}
		repo().delete(repo().findByName(cityName));
		return true;
	}

}
