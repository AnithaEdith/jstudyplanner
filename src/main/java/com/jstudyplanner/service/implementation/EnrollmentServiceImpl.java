package com.jstudyplanner.service.implementation;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.EnrollmentDAO;
import com.jstudyplanner.domain.CourseAvailability;
import com.jstudyplanner.domain.Enrollment;
import com.jstudyplanner.domain.Student;
import com.jstudyplanner.domain.User;
import com.jstudyplanner.service.EnrollmentService;


@Component("enrollmentService")
public class EnrollmentServiceImpl implements EnrollmentService {
	
	@Autowired EnrollmentDAO enrollmentDAO;

/*
	@Autowired
	private SessionFactory sessionFactory;
*/



	/**
	 * Attempts to save enrollment. Given enrollment can be new (without id) then it should be created in the database.
	 * OR the enrollment can be existing (with defined id) then it should be saved. If enrollment doesn't have status defined 
	 * then it should be retrieved from the database.
	 * Before saving enrollment should be checked that it is unique. Meaning there is no other enrollment with student, term, 
	 * course, campus that are the same.
	 */
	public void save(Enrollment enrollment) {
		Enrollment enrollmentToCheck = enrollmentDAO.getEnrollmentByCAAndStudent(enrollment.getCourseAvailability(), enrollment.getStudent());
		if (GeneralService.domainObjectsAreSame(enrollment, enrollmentToCheck)) {
			throw new CustomServiceException("", "Duplicate enrollment."
				+ " Student: " + enrollment.getStudent().getFullName()
				+ " Course: " + enrollment.getCourseAvailability().getCourse().getTitle()
				+ " Term: " + enrollment.getCourseAvailability().getTerm().getShortDescription()
				+ " Campus: " + enrollment.getCourseAvailability().getCampus().getTitle());
		}
		
		if ( enrollment.getId() == null ) {
			enrollmentDAO.add(enrollment);
		} else {
			if (enrollment.getStatus() == null) {
				Enrollment originalEnrollment = getEnrollmentById(enrollment.getId());
				enrollment.setStatus(originalEnrollment.getStatus());
			}
			
			enrollmentDAO.save(enrollment);
		}
	}

	
	/**
	 * Student should not be able to delete enrollment with status = 'enrolled'
	 */
	public void delete(Enrollment enrollment, User user) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		// TODO check all business constraints for enrollment delete (e.g. user permission)
		if (user.getUtype().equals("student") && enrollment.getStatus().equals("enrolled")) {
			throw new CustomServiceException("","Cannot delete enrollment because its status is enrolled (not planned).");
		}
		enrollmentDAO.delete(enrollment);
	}

	
	public Enrollment getEnrollmentById(Long id) {
		return enrollmentDAO.getEnrollmentById(id);
	}

	
	public Enrollment getEnrollmentByCAAndStudent(CourseAvailability ca, Student student) {
		return enrollmentDAO.getEnrollmentByCAAndStudent(ca, student);
	}

	
	public List<Enrollment> getAllEnrollments() {
		return enrollmentDAO.getAllEnrollments();
	}

	
	public List<Enrollment> getEnrollmentsByStatus(String status) {
		return enrollmentDAO.getEnrollmentsByStatus(status);
	}

	
	public List<Enrollment> getEnrollmentsByCA(CourseAvailability ca) {
		return enrollmentDAO.getEnrollmentsByCA(ca);
	}

	
	public List<Enrollment> getEnrollmentsByStudent(Student student) {
		return enrollmentDAO.getEnrollmentsByStudent(student);
	}

}
