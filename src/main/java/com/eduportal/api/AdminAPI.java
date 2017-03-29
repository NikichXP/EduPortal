package com.eduportal.api;

import com.eduportal.Text;
import com.eduportal.dao.GeoDAO;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.City;
import com.eduportal.entity.ClientEntity;
import com.eduportal.entity.Employee;
import com.eduportal.entity.UserEntity;
import com.eduportal.model.AccessSettings;
import com.eduportal.model.AuthContainer;
import com.eduportal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminAPI {
	
	/*
	 * Contains:
	 * # users
	 * # companies
	 * # city
	 */
	
	// Users here

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(path = "myEmployees", method = RequestMethod.GET)
	public List<Employee> myEmployees(@RequestParam("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getCorpEmployees(emp.getCorporation());
	}
	
	@RequestMapping(path = "myClients", method = RequestMethod.GET)
	public List<ClientEntity> myClients (@RequestParam("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getClients(emp);
	}
	
	@RequestMapping(path = "inactiveClients", method = RequestMethod.GET)
	public List<UserEntity> inactiveClients (@RequestParam("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getUnactiveClients(false);
	}
	
	@RequestMapping (path = "listByCompany", method = RequestMethod.GET) 
	public List<Employee> listByCompany (@RequestParam ("company") String companyName) {
		return UserDAO.getCorpEmployees(companyName);
	}
	
	// Companies
	
	@RequestMapping (path = "listCompanies", method = RequestMethod.GET)
	public Set<String> getCompanyList() {
		List<Employee> list = UserDAO.getAgentsList();
		Set<String> ret = new HashSet<>();
		for (Employee e : list) {
			ret.add(e.getCorporation());
		}
		return ret;
	}
	
	@RequestMapping (path = "cityList", method = RequestMethod.GET)
	public List<City> getCityList () {
		return GeoDAO.getCityList();
	}
	
	@RequestMapping (path = "createcity", method = RequestMethod.GET)
	public Text createcity (@RequestParam("city") String cityname, @RequestParam("country") String countryname, @RequestParam ("token") String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getAccessLevel() < AccessSettings.MODERATOR_LEVEL || !user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			return new Text ("Unauthorised");
		}
		City c = GeoDAO.createCity(cityname, countryname);
		return new Text ("Успешно создан город. Вернитесь назад");
	}
	
	@RequestMapping (path = "deletecity", method = RequestMethod.GET)
	public Text deleteCity (@RequestParam("city") String cityid, @RequestParam("token") String token) {
		boolean resp = GeoDAO.deleteCity(cityid);
		return new Text(!resp ? "City in use" : "Deleted");
	}

}
