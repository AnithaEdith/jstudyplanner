package com.jstudyplanner.service.implementation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.CourseDAO;
import com.jstudyplanner.domain.Course;
import com.jstudyplanner.service.CourseService;

@Component("courseService")
public class CourseServiceImpl implements CourseService {
	
	@Autowired CourseDAO courseDAO;

/*	@Autowired
	private SessionFactory sessionFactory;*/


	public List<Course> getAllCourses() {
		return courseDAO.getAllCourses();
	}

	public List<Course> getCoursesByStatus(boolean enabled) {
		return courseDAO.getCoursesByStatus(true);
	}
	
	public void save(Course course) {
		// TODO business checks if this course is prerequisite to others
		// TODO add localization to exception error message
		Course courseToCheck = courseDAO.getCourseByCode(course.getCode());
		if (GeneralService.domainObjectsAreSame(course, courseToCheck)) {
			throw new CustomServiceException("","There is already a course with code '" + course.getCode()
					+ "'. Course codes should be unique.");
		}
		
		courseToCheck = courseDAO.getCourseByTitle(course.getTitle());
		if (GeneralService.domainObjectsAreSame(course, courseToCheck)) {
			throw new CustomServiceException("","There is already a course with title '" + course.getTitle()
					+ "'. Course titles should be unique");
		}
		
		if ( course.getId() == null ) {
			courseDAO.add(course);
		} else {
			courseDAO.save(course);
		}
	}

	/**
	 * Delete given course. First remove all prerequisites (clear() and save()).
	 */
	public void delete(Course course) {
		// TODO Do all the business checks required to make sure that course can be deleted: major compulsory, program elective, program core.
		// if not throw exception with advice to delete required items first or disable the course.
		// Otherwise it throws Hibernate exceptions, - should be eliminated.
		
		course.getPrerequisites().clear(); // remove all prerequisites and update the course
		courseDAO.save(course);
		courseDAO.delete(course);
	}

	
	/**
	 * Use CourseDAO to find course by its id. If id is not defined then throw CustomServiceException
	 */
	public Course getCourseById(Long id) {
		if (id != null) {
			return courseDAO.getCourseById(id);
		} else {
			throw new CustomServiceException("","Course is not defined.");
		}
	}
	
	
	public Course getCourseByCode(String code) {
		return courseDAO.getCourseByCode(code);
	}

	
	/**
	 * Get list of courses that can be added to the given course.
	 * That includes all enabled courses that are not in the course's prerequisites set.
	 * @param courseID - given course ID
	 */
	public List<Course> getAvailablePrerequisites(Long courseID) {
		Course course = courseDAO.getCourseById(courseID);
		List<Course> result = courseDAO.getCoursesByStatus(true);
		
		Iterator<Course> iterator;
		iterator = result.iterator();
		Course currentCourse;
		
		// remove given course from result list
		while (iterator.hasNext()) {
			currentCourse = iterator.next();
			if (currentCourse.equals(course)) {
				iterator.remove();
				break;
			}
		}
		
		// iterate through prerequisites and remove them from result list 
		for (Course currentPrerequisite: course.getPrerequisites()) {
			iterator = result.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.equals(currentPrerequisite)) {
					iterator.remove();
					break; // remove course from result list and exit from inner loop 
				}
			}
		}
		
		return result;
	}
	

	/**
	 * Attempts to remove course prerequisites. Batch remove.
	 * It iterates through array of given prerequisite course IDs (outer for loop). Inside that iteration
	 * it iterates through given course's prerequisites to find one with matching id. Then it removes it
	 * from the prerequisites set. 
	 * @param courseId - given course ID
	 * @param prerequisiteIDs - array of IDs of courses to remove from prerequisites set
	 */
	public void removePrerequisites(Long courseID, Long[] prerequisiteIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Course course = courseDAO.getCourseById(courseID);
		Iterator<Course> iterator;
		Course currentPrerequisite;
		for (Long currentID: prerequisiteIDs) {
			iterator = course.getPrerequisites().iterator();
			while (iterator.hasNext()) {
				currentPrerequisite = iterator.next();
				if (currentPrerequisite.getId().equals(currentID)) {
					iterator.remove();
				}
			}
		}
		courseDAO.save(course);
	}


	/**
	 * Add prerequisites given by array of IDs to the given course (defined by course ID).
	 * Checks: this method should not add prerequisite if it is already there, and it cannot add course
	 * as a prerequisite to itself.
	 * @param courseID - given course ID
	 * @param prerequisiteIDs - array of IDs of courses to add to prerequisites set
	 */
	public void addPrerequisites(Long courseID, Long[] prerequisiteIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Course course = courseDAO.getCourseById(courseID);
		Set<Course> prerequisites = course.getPrerequisites();
		
		boolean found;
		Course courseToAdd;
		for (Long currentID: prerequisiteIDs) {
			found = false;
			if (currentID.equals(courseID)) {
				found = true;
				continue; // skip to next ID
			}
			for (Course prerequisite: prerequisites) {
				if (prerequisite.getId().equals(currentID)) {
					found = true;
					break; // exit inner loop
				}
			}
			if (found == false) {
				courseToAdd = courseDAO.getCourseById(currentID);
				prerequisites.add(courseToAdd);
			}
		}
		
		courseDAO.save(course);
	} // addPrerequisites
}