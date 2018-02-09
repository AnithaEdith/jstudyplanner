package com.jstudyplanner.web;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

import java.util.List;

@Controller
@RefreshScope
public class CampusController {
	Logger logger= LoggerFactory.getLogger(this.getClass());

	private final CampusService campusService;
	private final static int DEFAULT_PAGE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;
	private String campusServiceUrl;

	@Value("${campus.url}")
	private String campus_url;

	@Value("${campus.getall}")
	private String campus_getall;



	/**
	 * CampusService implementation should be marked with @Component
	 * @param campusService
	 */
	@Inject
	public CampusController(CampusService campusService) {
		this.campusService = campusService;
	}
	
	/**
	 * Get all Campuses, add to session, set pagination and return campus view.
	 * Set parameters (page, pageSize) if not defined.
	 */
	@RequestMapping(value = "/campuses/list", method = RequestMethod.GET)
	public ModelAndView listCampuses(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
		logger.info("inside listCampuses");
		String uri=campus_url+campus_getall;
		logger.info(uri);

		if (page == null) {
			page = 0;
		}
		// get enabled campuses instead of getAllCampuses
		PagedListHolder<Campus> resultList = (PagedListHolder<Campus>) session.getAttribute("CampusController_resultList");
		if (resultList == null) {
			ResponseEntity<List<Campus>> rateResponse =
					restTemplate.exchange(uri,
							HttpMethod.GET, null, new ParameterizedTypeReference<List<Campus>>() {
							});
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
	 * Get campus details and return details view.
	 * @param code campus code
	 * @param model
	 * @return details view
	 */
	@RequestMapping(value = "/campuses/{code:.+}", method = RequestMethod.GET)
	public String getCampusDetails(@PathVariable("code") String code, Model model) {
		model.addAttribute("campus", campusService.getCampusByCode(code));
		return "campuses/details";
	}
}