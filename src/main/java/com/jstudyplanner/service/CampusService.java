package com.jstudyplanner.service;

import com.jstudyplanner.domain.Campus;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines business rules for managing campuses
 */

public interface CampusService {
	public void save(Campus campus);
	public void delete(Campus campus);
	public Campus getCampusById(Long id);
	public Campus getCampusByCode(String code);
	public List<Campus> getAllCampuses();
	public List<Campus> getCampusesByStatus(boolean enabled);
}

