package com.jstudyplanner.web;


import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jstudyplanner.domain.Admin;
import com.jstudyplanner.domain.Campus;
import com.jstudyplanner.domain.Staff;
import com.jstudyplanner.domain.Student;
import com.jstudyplanner.service.CampusService;
import com.jstudyplanner.service.UserService;
import com.jstudyplanner.service.implementation.CustomServiceException;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AdminController {
	
	private final UserService userService;
	private final CampusService campusService;
	private final static int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * Controller constructor.
	 * Service objects implementations should be marked with @Component
	 */
	@Inject
	public AdminController(UserService userService, CampusService campusService) {
		this.userService = userService;
		this.campusService = campusService;
	}
	
	/**
	 * Default/home admin page
	 * @return
	 */
	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public String admin() {
		return "admin/home";
	}
	
	
	// List handlers ---------------------------------------------
	/**
	 * Get all Admin users, add to session, set pagination and return admin list view.
	 * Set parameters (page, pageSize) if not defined.
	 * 
	 * This method should only be available to admin user with username = 'admin' (ROLE_SUPERADMIN)
	 * Other types of users including ROLE_ADMIN shouldn't be able to manage admin accounts
	 */
	@Secured ({"ROLE_SUPERADMIN"})
	@RequestMapping(value = "/admin/adminlist", method = RequestMethod.GET)
	public ModelAndView listAdmins(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
		if (page == null) {
			page = 0;
		}
		
		PagedListHolder<Admin> resultList = (PagedListHolder<Admin>) session.getAttribute("AdminController_adminList");
		if (resultList == null) {
			resultList = new PagedListHolder<Admin>(userService.getAllAdmins());
			session.setAttribute("AdminController_adminList", resultList);
			if (pageSize == null) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
		if (pageSize != null) {
			resultList.setPageSize(pageSize);
		}
		resultList.setPage(page);
		return new ModelAndView("admin/adminlist", "resultList", resultList);
	} // listAdmins
	

	// TODO 1. implement refresh list functionality for everything
	// TODO 2. handle dots everywhere where path variable can possibly contain dots using regexp ':.+'
	/**
	 * Get all Staff users, add to session, set pagination and return staff list view.
	 * Set parameters (page, pageSize) if not defined.
	 */
	@RequestMapping(value = "/admin/stafflist", method = RequestMethod.GET)
	public ModelAndView listStaff(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
		if (page == null) {
			page = 0;
		}
		
		PagedListHolder<Staff> resultList = (PagedListHolder<Staff>) session.getAttribute("AdminController_staffList");
		if (resultList == null) {
			resultList = new PagedListHolder<Staff>(userService.getAllStaff());
			session.setAttribute("AdminController_staffList", resultList);
			if (pageSize == null) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
		if (pageSize != null) {
			resultList.setPageSize(pageSize);
		}
		resultList.setPage(page);
		return new ModelAndView("admin/stafflist", "resultList", resultList);
	} // listStaff
	
	
	/**
	 * Get all Student users, add to session, set pagination and return student list view.
	 * Set parameters (page, pageSize) if not defined.
	 */
	@RequestMapping(value = "/admin/studentlist", method = RequestMethod.GET)
	public ModelAndView listStudents(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
		if (page == null) {
			page = 0;
		}
		
		PagedListHolder<Student> resultList = (PagedListHolder<Student>) session.getAttribute("AdminController_studentList");
		if (resultList == null) {
			resultList = new PagedListHolder<Student>(userService.getAllStudents());
			session.setAttribute("AdminController_studentList", resultList);
			if (pageSize == null) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
		if (pageSize != null) {
			resultList.setPageSize(pageSize);
		}
		resultList.setPage(page);
		return new ModelAndView("admin/studentlist", "resultList", resultList);
	} // listStudents
	
	
	/**
	 * Get all Campuses, add to session, set pagination and return student list view.
	 * Set parameters (page, pageSize) if not defined.
	 */
	@RequestMapping(value = "/admin/campuslist", method = RequestMethod.GET)
	public ModelAndView listCampuses(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, HttpSession session) {
		if (page == null) {
			page = 0;
		}
		
		PagedListHolder<Campus> resultList = (PagedListHolder<Campus>) session.getAttribute("AdminController_campusList");
		if (resultList == null) {
			resultList = new PagedListHolder<Campus>(campusService.getAllCampuses());
			session.setAttribute("AdminController_campusList", resultList);
			if (pageSize == null) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
		if (pageSize != null) {
			resultList.setPageSize(pageSize);
		}
		resultList.setPage(page);
		return new ModelAndView("admin/campuslist", "resultList", resultList);
	} // listCampuses
	
	
	
	
	
	// Edit / Create methods ----------------------------------------
	
	/**
	 * Redirect to Admin Create page (form)
	 */
	@RequestMapping(value = "/admin/admincreate", method = RequestMethod.GET)
	public String redirectToAdminCreate(Model model) {
		Admin adminUser = new Admin();
		model.addAttribute("adminUser", adminUser);
		return "admin/admincreate";
	}
	
	/**
	 * Redirect to Staff Create page (form)
	 */
	@RequestMapping(value = "/admin/staffcreate", method = RequestMethod.GET)
	public String redirectToStaffCreate(Model model) {
		Staff staffUser = new Staff();
		model.addAttribute("staffUser", staffUser);
		return "admin/staffcreate";
	}
	
	/**
	 * Redirect to Student Create page (form)
	 */
	@RequestMapping(value = "/admin/studentcreate", method = RequestMethod.GET)
	public String redirectToStudentCreate(Model model) {
		Student studentUser = new Student();
		model.addAttribute("studentUser", studentUser);
		return "admin/studentcreate";
	}
	
	/**
	 * Redirect to Campus Create page (form)
	 */
	@RequestMapping(value = "/admin/campuscreate", method = RequestMethod.GET)
	public String redirectToCampusCreate(Model model) {
		Campus campus = new Campus();
		model.addAttribute("campus", campus);
		return "admin/campuscreate";
	}
	
	
	/**
	 * Redirect to Admin Edit/Delete page (form) for admin identified by username parameter
	 * ':.+' regexp added to handle usernames with dots
	 */
	@RequestMapping(value = "/admin/adminedit/{username:.+}", method = RequestMethod.GET)
	public String redirectToAdminEditDelete(@PathVariable("username") String username, Model model) {
		Admin adminUser = (Admin) userService.getUserByUsername(username);
		model.addAttribute("adminUser", adminUser);
		// TODO handle if adminuser not found
		return "admin/adminedit";
	}
	
	/**
	 * Redirect to Staff Edit/Delete page (form) for staff identified by username parameter
	 */
	@RequestMapping(value = "/admin/staffedit/{username:.+}", method = RequestMethod.GET)
	public String redirectToStaffEditDelete(@PathVariable("username") String username, Model model) {
		Staff staffUser = (Staff) userService.getUserByUsername(username);
		model.addAttribute("staffUser", staffUser);
		// TODO handle if staffuser not found
		return "admin/staffedit";
	}
	
	/**
	 * Redirect to Student Edit/Delete page (form) for student identified by username parameter
	 */
	@RequestMapping(value = "/admin/studentedit/{username:.+}", method = RequestMethod.GET)
	public String redirectToStudentEditDelete(@PathVariable("username") String username, Model model) {
		Student studentUser = (Student) userService.getUserByUsername(username);
		model.addAttribute("studentUser", studentUser);
		// TODO handle if studentuser not found
		return "admin/studentedit";
	}
	
	/**
	 * Redirect to Campus Edit/Delete page (form) for campus identified by campus code parameter
	 */
	@RequestMapping(value = "/admin/campusedit/{code:.+}", method = RequestMethod.GET)
	public String redirectToCampusEditDelete(@PathVariable("code") String code, Model model) {
		Campus campus = (Campus) campusService.getCampusByCode(code);
		model.addAttribute("campus", campus);
		// TODO handle if campus not found
		return "admin/campusedit";
	}
	
	
	/**
	 * Delete admin user by username. Update admin list after deletion.
	 */
	@RequestMapping(value = "/admin/admindelete/{username:.+}", method = RequestMethod.GET)
	public ModelAndView deleteAdmin(@PathVariable("username") String username, HttpSession session) {
		// TODO logic for delete admin if not successful
		userService.delete(username);
		ModelAndView modelAndView = new ModelAndView("admin/adminlist");
		
		String infoMessage = "Admin user '" + username + "' deleted successfully.";
		modelAndView.addObject("infoMessage", infoMessage);
		
		PagedListHolder<Admin> resultList = new PagedListHolder<Admin>(userService.getAllAdmins());
		modelAndView.addObject("resultList", resultList);
		session.setAttribute("AdminController_adminList", resultList);
		
		return modelAndView;
	}
	
	/**
	 * Delete staff user by username. Update staff list after deletion.
	 */
	@RequestMapping(value = "/admin/staffdelete/{username:.+}", method = RequestMethod.GET)
	public ModelAndView deleteStaff(@PathVariable("username") String username, HttpSession session) {
		// TODO logic for delete staff if not successful
		userService.delete(username);
		ModelAndView modelAndView = new ModelAndView("admin/stafflist");
		
		String infoMessage = "Staff user '" + username + "' deleted successfully.";
		modelAndView.addObject("infoMessage", infoMessage);
		
		PagedListHolder<Staff> resultList = new PagedListHolder<Staff>(userService.getAllStaff());
		modelAndView.addObject("resultList", resultList);
		session.setAttribute("AdminController_staffList", resultList);
		
		return modelAndView;
	}
	
	/**
	 * Delete student user by username. Update student list after deletion.
	 */
	@RequestMapping(value = "/admin/studentdelete/{username:.+}", method = RequestMethod.GET)
	public ModelAndView deleteStudent(@PathVariable("username") String username, HttpSession session) {
		// TODO logic for delete student if not successful
		userService.delete(username);
		ModelAndView modelAndView = new ModelAndView("admin/studentlist");
		
		String infoMessage = "Student user '" + username + "' deleted successfully.";
		modelAndView.addObject("infoMessage", infoMessage);
		
		PagedListHolder<Student> resultList = new PagedListHolder<Student>(userService.getAllStudents());
		modelAndView.addObject("resultList", resultList);
		session.setAttribute("AdminController_studentList", resultList);
		
		return modelAndView;
	}
	
	/**
	 * Delete campus by code. Update campus list after deletion.
	 */
	@RequestMapping(value = "/admin/campusdelete/{code:.+}", method = RequestMethod.GET)
	public ModelAndView deleteCampus(@PathVariable("code") String code, HttpSession session) {
		Campus campus = campusService.getCampusByCode(code); // TODO handle if not found
		
		// TODO logic for delete campus if not successful
		campusService.delete(campus);
		ModelAndView modelAndView = new ModelAndView("admin/campuslist");
		
		String infoMessage = "Campus '" + campus.getTitle() + "' deleted successfully.";
		modelAndView.addObject("infoMessage", infoMessage);
		
		PagedListHolder<Campus> resultList = new PagedListHolder<Campus>(campusService.getAllCampuses());
		modelAndView.addObject("resultList", resultList);
		session.setAttribute("AdminController_campusList", resultList);
		
		return modelAndView;
	}
	
	
	/**
	 * Updates/creates admin user. If admin user updated/created successfully then open admin user list (also refresh it).
	 * If update/create was not successful then redirect back to the edit/create page and display error message.
	 */
	@RequestMapping(value = "/admin/adminupdate", method = RequestMethod.POST)
	public ModelAndView updateAdmin(@ModelAttribute Admin adminUser, HttpSession session) {
		ModelAndView modelAndView;
		try {
			userService.save(adminUser);
			
			// if update was successful
			modelAndView = new ModelAndView("admin/adminlist");
			String infoMessage = "Admin user '" + adminUser.getUsername() + "' saved successfully.";
			modelAndView.addObject("infoMessage", infoMessage);
			
			PagedListHolder<Admin> resultList = new PagedListHolder<Admin>(userService.getAllAdmins());
			modelAndView.addObject("resultList", resultList);
			session.setAttribute("AdminController_adminList", resultList);
		} catch (CustomServiceException ex) {
			if (adminUser.getId() == null) {
				modelAndView = new ModelAndView("admin/admincreate");
			} else {
				modelAndView = new ModelAndView("admin/adminedit");
			}
			modelAndView = new ModelAndView("admin/adminedit");
			modelAndView.addObject("adminUser", adminUser);
			modelAndView.addObject("errorMessage", ex.getErrorMsg());
		}
		
		return modelAndView;
	}
	
	/**
	 * Updates/creates staff user. If staff user updated/created successfully then open staff user list (also refresh it).
	 * If update/create was not successful then redirect back to the edit/create page and display error message.
	 */
	@RequestMapping(value = "/admin/staffupdate", method = RequestMethod.POST)
	public ModelAndView updateStaff(@ModelAttribute Staff staffUser, HttpSession session) {
		ModelAndView modelAndView;
		try {
			userService.save(staffUser);
			
			// if update was successful
			modelAndView = new ModelAndView("admin/stafflist");
			String infoMessage = "Staff user '" + staffUser.getUsername() + "' saved successfully.";
			modelAndView.addObject("infoMessage", infoMessage);
			
			PagedListHolder<Staff> resultList = new PagedListHolder<Staff>(userService.getAllStaff());
			modelAndView.addObject("resultList", resultList);
			session.setAttribute("AdminController_staffList", resultList);
		} catch (CustomServiceException ex) {
			if (staffUser.getId() == null) {
				modelAndView = new ModelAndView("admin/staffcreate");
			} else {
				modelAndView = new ModelAndView("admin/staffedit");
			}
			modelAndView.addObject("staffUser", staffUser);
			modelAndView.addObject("errorMessage", ex.getErrorMsg());
		}
		
		return modelAndView;
	}
	
	/**
	 * Updates/creates student user. If student user updated/created successfully then open student user list (also refresh it).
	 * If update/create was not successful then redirect back to the edit/create page and display error message.
	 */
	@RequestMapping(value = "/admin/studentupdate", method = RequestMethod.POST)
	public ModelAndView updateStudent(@ModelAttribute Student studentUser, HttpSession session) {
		ModelAndView modelAndView;
		try {
			userService.save(studentUser);
			
			// if update was successful
			modelAndView = new ModelAndView("admin/studentlist");
			String infoMessage = "Student user '" + studentUser.getUsername() + "' saved successfully.";
			modelAndView.addObject("infoMessage", infoMessage);
			
			PagedListHolder<Student> resultList = new PagedListHolder<Student>(userService.getAllStudents());
			modelAndView.addObject("resultList", resultList);
			session.setAttribute("AdminController_studentList", resultList);
		} catch (CustomServiceException ex) {
			if (studentUser.getId() == null) {
				modelAndView = new ModelAndView("admin/studentcreate");
			} else {
				modelAndView = new ModelAndView("admin/studentedit");
			}
			modelAndView.addObject("studentUser", studentUser);
			modelAndView.addObject("errorMessage", ex.getErrorMsg());
		}
		
		return modelAndView;
	}
	
	/**
	 * Updates/creates campus. If campus updated/created successfully then open campus list (also refresh it).
	 * If update/create was not successful then redirect back to the edit/create page and display error message.
	 */
	@RequestMapping(value = "/admin/campusupdate", method = RequestMethod.POST)
	public ModelAndView updateCampus(@ModelAttribute Campus campus, HttpSession session) {
		ModelAndView modelAndView;
		try {
			campusService.save(campus);
			
			// if update/create was successful
			modelAndView = new ModelAndView("admin/campuslist");
			String infoMessage = "Campus '" + campus.getTitle() + "' saved successfully.";
			modelAndView.addObject("infoMessage", infoMessage);
			
			PagedListHolder<Campus> resultList = new PagedListHolder<Campus>(campusService.getAllCampuses());
			modelAndView.addObject("resultList", resultList);
			session.setAttribute("AdminController_campusList", resultList);
		} catch (CustomServiceException ex) {
			if (campus.getId() == null) {
				modelAndView = new ModelAndView("admin/campuscreate");
			} else {
				modelAndView = new ModelAndView("admin/campusedit");
			}
			modelAndView.addObject("campus", campus);
			modelAndView.addObject("errorMessage", ex.getErrorMsg());
		}
		
		return modelAndView;
	}
	
}