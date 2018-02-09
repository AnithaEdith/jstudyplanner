package com.jstudyplanner.service.implementation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.CourseDAO;
import com.jstudyplanner.dao.MajorDAO;
import com.jstudyplanner.domain.Course;
import com.jstudyplanner.domain.Major;
import com.jstudyplanner.service.MajorService;

@Component("majorService")
public class MajorServiceImpl implements MajorService {
	
	@Autowired MajorDAO majorDAO;
	@Autowired CourseDAO courseDAO;

	public List<Major> getAllMajors() {
		return majorDAO.getAllMajors();
	}

	/**
	 * Attempts to save/update major.
	 * Check if code is unique. And combination of title - program is unique. 
	 * If not throw an exception. Else save/update this major.
	 * Note that current business requirement allows 2 or more majors with same title but different programs,
	 * e.g. 'Software Development' major in both bachelor and master IT programs.
	 * If major doesn't have id then this is a new one - persist it. Else update existing major. 
	 */
	public void save(Major major) {
		// TODO business checks for compulsory courses
		// TODO add localization to exception error message
		// TODO add error codes
		Major majorToCheck = majorDAO.getMajorByCode(major.getCode());
		if (GeneralService.domainObjectsAreSame(major, majorToCheck)) {
			throw new CustomServiceException("","There is already a major with code '" + major.getCode() + "'. Major codes should be unique.");
		}
		
		majorToCheck = majorDAO.getMajorByTitleAndProgram(major.getTitle(), major.getProgram());
		if (GeneralService.domainObjectsAreSame(major, majorToCheck)) {
			throw new CustomServiceException("","There is already a major with title '" + major.getTitle() 
					+ "' and program '" + major.getProgram().getTitle() + "'. A program should not have more than"
					+ " one major with the same title.");
		}
		
		if ( major.getId() == null ) {
			majorDAO.add(major);
		} else {
			majorDAO.save(major);
		}
	} // saveMajor

	
	public void delete(Major major) {
		// TODO Do all the business checks required to make sure that major can be deleted.
		// if not throw exception with advice to delete required items first or disable the major.
		majorDAO.delete(major);
	}

	public Major getMajorById(Long id) {
		if (id != null) {
			return majorDAO.getMajorById(id);
		} else {
			throw new CustomServiceException("","Major is not defined.");
		}
	}

	public Major getMajorByCode(String code) {
		return majorDAO.getMajorByCode(code);
	}

	
	/**
	 * Get list of courses that can be added to the given major as compulsory courses.
	 * That includes all enabled courses that are 
	 * 1. not in the major's compulsory courses set;
	 * 2. not in the major's program core courses set.
	 * @param majorID - given major ID
	 */
	public List<Course> getAvailableCompulsoryCourses(Long majorID) {
		Major major = majorDAO.getMajorById(majorID);
		List<Course> result = courseDAO.getCoursesByStatus(true);
		
		Iterator<Course> iterator;
		Course currentCourse;
		
		// iterate through compulsory courses and remove them from result list 
		for (Course currentCompulsoryCourse: major.getCompulsoryCourses()) {
			iterator = result.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.equals(currentCompulsoryCourse)) {
					iterator.remove();
					break; // remove course from result list and exit inner loop 
				}
			}
		}
		
		// iterate through major's program core courses and remove them from result list 
		for (Course currentCoreCourse: major.getProgram().getCoreCourses()) {
			iterator = result.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.equals(currentCoreCourse)) {
					iterator.remove();
					break; // remove course from result list and exit inner loop 
				}
			}
		}
		
		return result;
	}
	

	/**
	 * Attempts to remove compulsory courses. Batch remove.
	 * It iterates through array of given course IDs (outer for loop). Inside that iteration it iterates 
	 * through given major's compulsory courses to find one with matching id. Then it removes it
	 * from the compulsory courses set. 
	 * @param majorID - given major ID
	 * @param courseIDs - array of IDs of courses to remove from compulsory courses set
	 */
	public void removeCompulsoryCourses(Long majorID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Major major = majorDAO.getMajorById(majorID);
		Iterator<Course> iterator;
		Course currentCourse;
		Set<Course> compulsoryCourses = major.getCompulsoryCourses();
		for (Long currentID: courseIDs) {
			iterator = compulsoryCourses.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.getId().equals(currentID)) {
					iterator.remove();
				}
			}
		}
		majorDAO.save(major);
	}


	/**
	 * Add compulsory courses given by array of IDs to the given major (defined by major ID).
	 * Checks: this method should not add compulsory course if it is already there.
	 * @param majorID - given major ID
	 * @param courseIDs - array of IDs of courses to add to compulsory courses set
	 */
	public void addCompulsoryCourses(Long majorID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Major major = majorDAO.getMajorById(majorID);
		Set<Course> compulsoryCourses = major.getCompulsoryCourses();
		
		boolean found;
		Course courseToAdd;
		for (Long currentID: courseIDs) {
			found = false; // current course is found in compulsory courses
			
			for (Course course: compulsoryCourses) {
				if (course.getId().equals(currentID)) {
					found = true;
					break; // exit inner loop
				}
			}
			if (found == false) {
				courseToAdd = courseDAO.getCourseById(currentID);
				compulsoryCourses.add(courseToAdd);
			}
		}
		
		majorDAO.save(major);
	}
}