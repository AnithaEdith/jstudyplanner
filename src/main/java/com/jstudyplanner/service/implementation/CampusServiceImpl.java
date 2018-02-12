package com.jstudyplanner.service.implementation;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.CampusDAO;
import com.jstudyplanner.domain.Campus;
import com.jstudyplanner.service.CampusService;


/**
 * 
 * @author oleg, oleglukin@yahoo.com
 */
@Component("campusService")
public class CampusServiceImpl implements CampusService {
	
	@Autowired CampusDAO campusDAO;

	public List<Campus> getAllCampuses() {
		return campusDAO.getAllCampuses();
	}
	
	public List<Campus> getCampusesByStatus(boolean enabled) {
		return campusDAO.getCampusesByStatus(enabled);
	}

	public void save(Campus campus) {
		// TODO add localization to exception error message
		// TODO add error codes
		Campus campusToCheck = campusDAO.getCampusByCode(campus.getCode());
		if (GeneralService.domainObjectsAreSame(campus, campusToCheck)) {
			throw new CustomServiceException("","There is already a campus with code '" + campus.getCode() + "'. Campus code should be unique.");
		}
		
		campusToCheck = campusDAO.getCampusByTitle(campus.getTitle());
		if (GeneralService.domainObjectsAreSame(campus, campusToCheck)) {
			throw new CustomServiceException("","There is already a campus named '" + campus.getTitle() + "'. Campus title should be unique.");
		}
		
		if ( campus.getId() == null ) {
			campusDAO.add(campus);
		} else {
			campusDAO.save(campus);
		}
	}

	public void delete(Campus campus) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		// TODO check all business constraints for campus delete (e.g. course availability, students enrolled)
		campusDAO.delete(campus);
	}

	public Campus getCampusById(Long id) {
		return campusDAO.getCampusById(id);
	}

	public Campus getCampusByCode(String code) {
		return campusDAO.getCampusByCode(code);
	}
}