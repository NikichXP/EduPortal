package com.eduportal.entity;

import com.eduportal.dao.GeoDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class Product {

	@Id
	private String id;
	private static final long serialVersionUID = 8913130263468249461L;
	private String title;
	private String description;
	private String cityId;
	private boolean actual;
	private double defaultPrice;
	private String start;
	private String end;
	private ArrayList<SavedFile> files;
	private String currency;
	private String provider;
	
	public Product () {
		files = new ArrayList<>();
		this.start = "01/09";
		this.end = "31/05";
	}
	
	public Product (String name, String descr, City c) {
		super();
		this.title = name;
		this.description = descr;
		this.cityId = c.getId();
		files = new ArrayList<>();
		this.start = "01/09";
		this.end = "31/05";
	}
	
	public City cityEntity() {
		return GeoDAO.getCityById(cityId);
	}
	
	public void defineCity(City city) {
		this.cityId = city.getId();
	}

	public Product setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
		return this;
	}
	
	public void addFile (SavedFile file) {
		this.files.add(file);
	}

	public void updateIfNotNull(String title, String descr, Double price) {
		if (title != null) {
			this.title = title;
		}
		if (descr != null) {
			this.description = descr;
		}
		if (price != null) {
			this.defaultPrice = price;
		}
	}

}
