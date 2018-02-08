package com.jstudyplanner.service.implementation;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.UserDAO;
import com.jstudyplanner.domain.Admin;
import com.jstudyplanner.domain.Staff;
import com.jstudyplanner.domain.Student;
import com.jstudyplanner.domain.User;
import com.jstudyplanner.service.UserService;


@Component("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired UserDAO userDAO;

	/*@Autowired
	private SessionFactory sessionFactory;*/


	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Use userDAO's method to get list of Admin users
	 * The result list should exclude superadmin - user who has username = 'admin'
	 * because superadmin shouldn't see his/her own name in the list.
	 */
	public List<Admin> getAllAdmins() {
		List<Admin> result = userDAO.getUsersByType("admin");
		for (Admin current: result ) {
			if (current.getUsername().equals("admin")) {
				// remove superadmin and exit loop
				result.remove(current);
				break;
			}
		}
		return result;
	}

	
	/**
	 * Use userDAO's method to get list of Staff users
	 */
	public List<Staff> getAllStaff() {
		return userDAO.getUsersByType("staff");
	}
	
	
	/**
	 * Use userDAO's method to get list of Student users
	 */
	public List<Student> getAllStudents() {
		return userDAO.getUsersByType("student");
	}
	
	
	/**
	 * Attempts to save/update user.
	 * Check if username and email are unique. If not throw exception. Else save/update this user.
	 * If user doesn't have id then this is a new one - persist it. Else update existing user. 
	 */
	public void save(User user) throws CustomServiceException {
		// TODO add localization to exception error message
		// TODO add error codes
		User userToCheck = userDAO.getUserByUsername(user.getUsername());
		if (GeneralService.domainObjectsAreSame(user, userToCheck)) {
			throw new CustomServiceException("","There is already a user with username '" + user.getUsername() + "'. Username should be unique.");
		}
		
		userToCheck = userDAO.getUserByEmail(user.getEmail());
		if (GeneralService.domainObjectsAreSame(user, userToCheck)) {
			throw new CustomServiceException("","There is already a user with email '" + user.getEmail() + "'. Email should be unique.");
		}
		
		if ( user.getId() == null ) {
			userDAO.add(user);
		} else {
			userDAO.save(user);
		}
	} // saveUser
	

	public void delete(User user) {
		// TODO Auto-generated method stub
	}
	
	public void delete(String username) {
		// TODO throw exception if not found or unsuccessful
		// TODO check all business constraints for student and staff delete
		userDAO.delete(username);
	}

	public User getUserById(Long id) {
		// TODO implement method getUserById(Long id)
		return null;
	}

	public User getUserByUsername(String username) {
		// TODO throw exception if not found
		return userDAO.getUserByUsername(username);
	}

	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getUsersByLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getUsersByFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getEnabledOrDisabledUsers(boolean enabled) {
		// TODO Auto-generated method stub
		return null;
	}
	
}