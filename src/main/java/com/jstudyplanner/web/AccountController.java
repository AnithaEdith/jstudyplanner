package com.jstudyplanner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class AccountController {
	
	
	@RequestMapping(value = "/logoutPage", method = RequestMethod.GET)
    public String logout() {
        return "logoutPage";
    }
     

    
    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    public String loginPage() {
        return "loginPage";
    }
	
}