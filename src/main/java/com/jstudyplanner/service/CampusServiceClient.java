package com.jstudyplanner.service;

import com.jstudyplanner.domain.Campus;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CampusServiceClient {

        private Logger logger = LoggerFactory.getLogger(getClass());
        private RestTemplate restTemplate;

        @Autowired
        public CampusServiceClient(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @HystrixCommand(fallbackMethod = "defaultcampuslist")
        public ResponseEntity<List<Campus>> getCampusList(String uri) {
            ResponseEntity<List<Campus>> rateResponse =restTemplate.exchange(uri, HttpMethod.GET,  null, new ParameterizedTypeReference<List<Campus>>() {});
            return rateResponse;
        }

        public ResponseEntity<List<Campus>>  defaultcampuslist(String uri) {
            List<Campus> campuses= new ArrayList<Campus>();
            Campus campus= new Campus();
            campus.setId(60L);
            campus.setAddress("add1");
            campus.setDescription("desc1");
            campuses.add(campus);
            logger.debug("Default fortune used.");
            return ResponseEntity.ok(campuses);
        }

        public ResponseEntity<Campus> getCampusByCode(String uri, String code){
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(uri)
                    .queryParam("code", code);


            Campus rateResponse =restTemplate.getForObject(uri.toString(),   Campus.class);
            return ResponseEntity.ok(rateResponse);

        }

}
