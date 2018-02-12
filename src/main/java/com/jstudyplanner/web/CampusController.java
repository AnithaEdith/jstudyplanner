package com.jstudyplanner.web;


import javax.servlet.http.HttpSession;

import com.jstudyplanner.service.CampusServiceClient;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.jstudyplanner.domain.Campus;
import com.jstudyplanner.service.CampusService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RefreshScope
public class CampusController {
	Logger logger= LoggerFactory.getLogger(this.getClass());

	//private final CampusService campusService;
	private final static int DEFAULT_PAGE_SIZE = 10;
	private EurekaClient discoveryClient;


	private String campusServiceUrl;

	@Value("${campus.url}")
	private String campus_url;

	@Value("${campus.getall}")
	private String campus_getall;

    @Value("${campus.getbycode}")
    private String campus_getbycode;

    @Autowired
    CampusServiceClient campusService;

	/**
	 * CampusService implementation should be marked with @Component
	 */
	/*@Inject
	public CampusController(CampusService campusService) {
		this.campusService = campusService;
	}*/

	@Autowired
	public CampusController(EurekaClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	//	this.restTemplate = new RestTemplate();
	}
	
	/**
	 * Get all Campuses, add to session, set pagination and return campus view.
	 * Set parameters (page, pageSize) if not defined.
	 */
	@RequestMapping(value = "/campuses/list", method = RequestMethod.GET)
	public ModelAndView listCampuses(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
/*
        Using local uri of Campus service
		logger.info("inside listCampuses");
		String uri=campus_url+campus_getall;
		logger.info(uri);
*/

		if (page == null) {
			page = 0;
		}
		// get enabled campuses instead of getAllCampuses
		PagedListHolder<Campus> resultList = (PagedListHolder<Campus>) session.getAttribute("CampusController_resultList");
		if (resultList == null) {
            /*        Using Discovery client

            String uri=fetchCampusServiceUrl()+campus_getall;
            logger.info(uri);
*/

            /*Using Ribbon*/
            String uri="http://campus-service/"+campus_getall;
            logger.info(uri);

            ResponseEntity<List<Campus>> rateResponse =campusService.getCampusList(uri);


			List<Campus> campuses = rateResponse.getBody();
			logger.info("fetched campuses" +campuses.size());
			//resultList = new PagedListHolder<Campus>(campusService.getAllCampuses());
			resultList = new PagedListHolder<Campus>(campuses);
			session.setAttribute("CampusController_resultList", resultList);
			if (pageSize == null) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
		if (pageSize != null) {
			resultList.setPageSize(pageSize);
		}
		resultList.setPage(page);
		return new ModelAndView("campuses/list", "resultList", resultList);
	} // listCampuses




    /**
     * Using Discovery Client
     * */
	private String fetchCampusServiceUrl() {
		InstanceInfo instance = discoveryClient.getNextServerFromEureka("CAMPUS-SERVICE", false);
		logger.debug("instanceID: {}", instance.getId());

		String campusServiceUrl = instance.getHomePageUrl();
		logger.debug("Campus service homePageUrl: {}", campusServiceUrl);

		return campusServiceUrl;
	}

	/**
	 * Get campus details and return details view.
	 * @param code campus code
	 * @param model
	 * @return details view
	 */
	/*@RequestMapping(value = "/campuses/{code:.+}", method = RequestMethod.GET)
	public String getCampusDetails(@PathVariable("code") String code, Model model) {
        String uri="http://campus-service/"+campus_getall;
        logger.info(uri);

        model.addAttribute("campus", campusService.getCampusByCode(uri, code));
		return "campuses/details";
	}*/
}