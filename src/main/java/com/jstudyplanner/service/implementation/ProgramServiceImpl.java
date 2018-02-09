package com.jstudyplanner.service.implementation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.CourseDAO;
import com.jstudyplanner.dao.ProgramDAO;
import com.jstudyplanner.domain.Course;
import com.jstudyplanner.domain.Major;
import com.jstudyplanner.domain.Program;
import com.jstudyplanner.service.ProgramService;

@Component("programService")
public class ProgramServiceImpl implements ProgramService {
	
	@Autowired ProgramDAO programDAO;
	@Autowired CourseDAO courseDAO;

	public List<Program> getAllPrograms() {
		return programDAO.getAllPrograms();
	}
	
	public List<Program> getProgramsByStatus(boolean enabled) {
		return programDAO.getProgramsByStatus(enabled);
	}

	/**
	 * Attempts to save/update program.
	 * Check if code and title are unique. If not throw exception. Else save/update this program.
	 * If program doesn't have id then this is a new one - persist it. Else update existing program. 
	 */
	public void save(Program program) {
		// TODO business checks for core and elective courses
		// TODO add localization to exception error message
		// TODO add error codes
		Program programToCheck = programDAO.getProgramByCode(program.getCode());
		if (GeneralService.domainObjectsAreSame(program, programToCheck)) {
			throw new CustomServiceException("","There is already a program with code '" + program.getCode() + "'. Program codes should be unique.");
		}
		
		programToCheck = programDAO.getProgramByTitle(program.getTitle());
		if (GeneralService.domainObjectsAreSame(program, programToCheck)) {
			throw new CustomServiceException("","There is already a program with title '" + program.getTitle() + "'. Program title should be unique.");
		}
		
		if ( program.getId() == null ) {
			programDAO.add(program);
		} else {
			programDAO.save(program);
		}
	} // saveProgram


	public void deleteProgram(Program program) {
		// TODO Do all the business checks required to make sure that program can be deleted.
		// if not throw exception with advice to delete required items first or disable the program.
		programDAO.delete(program);
	}

	public Program getProgramById(Long id) {
		return programDAO.getProgramById(id);
	}

	public Program getProgramByCode(String code) {
		return programDAO.getProgramByCode(code);
	}

	@Override
	public List<Major> getProgramMajors(Program program) {
		return programDAO.getProgramMajors(program);
	}


	/**
	 * Get list of courses that can be added to the given program as core courses.
	 * That includes all enabled courses that are not in the program's core courses set.
	 * @param programID - given program ID
	 */
	public List<Course> getAvailableCoreCourses(Long programID) {
		Program program = programDAO.getProgramById(programID);
		List<Course> result = courseDAO.getCoursesByStatus(true);
		
		Iterator<Course> iterator;
		Course currentCourse;
		
		// iterate through core courses and remove them from result list 
		for (Course currentCoreCourse: program.getCoreCourses()) {
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
	 * Attempts to remove core courses. Batch remove.
	 * It iterates through array of given course IDs (outer for loop). Inside that iteration it iterates 
	 * through given program's core courses to find one with matching id. Then it removes it
	 * from the core courses set. 
	 * @param programID - given program ID
	 * @param courseIDs - array of IDs of courses to remove from core courses set
	 */
	public void removeCoreCourses(Long programID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Program program = programDAO.getProgramById(programID);
		Iterator<Course> iterator;
		Course currentCourse;
		Set<Course> coreCourses = program.getCoreCourses();
		for (Long currentID: courseIDs) {
			iterator = coreCourses.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.getId().equals(currentID)) {
					iterator.remove();
				}
			}
		}
		programDAO.save(program);
	}


	/**
	 * Add core courses given by array of IDs to the given program (defined by program ID).
	 * Checks: this method should not add core course if it is already there.
	 * @param programID - given program ID
	 * @param courseIDs - array of IDs of courses to add to core courses set
	 */
	public void addCoreCourses(Long programID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Program program = programDAO.getProgramById(programID);
		Set<Course> coreCourses = program.getCoreCourses();
		
		boolean found;
		Course courseToAdd;
		for (Long currentID: courseIDs) {
			found = false; // current course is found in core courses
			
			for (Course course: coreCourses) {
				if (course.getId().equals(currentID)) {
					found = true;
					break; // exit inner loop
				}
			}
			if (found == false) {
				courseToAdd = courseDAO.getCourseById(currentID);
				coreCourses.add(courseToAdd);
			}
		}
		
		programDAO.save(program);
	}

	

	/**
	 * Get list of courses that can be added to the given program as elective courses.
	 * That includes all enabled courses that are not in the program's elective courses set.
	 * @param programID - given program ID
	 */
	public List<Course> getAvailableElectiveCourses(Long programID) {
		Program program = programDAO.getProgramById(programID);
		List<Course> result = courseDAO.getCoursesByStatus(true);
		
		Iterator<Course> iterator;
		Course currentCourse;
		
		// iterate through elective courses and remove them from result list
		for (Course currentElectiveCourse: program.getElectiveCourses()) {
			iterator = result.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.equals(currentElectiveCourse)) {
					iterator.remove();
					break; // remove course from result list and exit inner loop 
				}
			}
		}
		
		// iterate through core courses and remove them from result list
		for (Course currentCoreCourse: program.getCoreCourses()) {
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
	 * Attempts to remove elective courses. Batch remove.
	 * It iterates through array of given course IDs (outer for loop). Inside that iteration it iterates 
	 * through given program's elective courses to find one with matching id. Then it removes it
	 * from the elective courses set. 
	 * @param programID - given program ID
	 * @param courseIDs - array of IDs of courses to remove from elective courses set
	 */
	public void removeElectiveCourses(Long programID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Program program = programDAO.getProgramById(programID);
		Iterator<Course> iterator;
		Course currentCourse;
		Set<Course> electiveCourses = program.getElectiveCourses();
		for (Long currentID: courseIDs) {
			iterator = electiveCourses.iterator();
			while (iterator.hasNext()) {
				currentCourse = iterator.next();
				if (currentCourse.getId().equals(currentID)) {
					iterator.remove();
				}
			}
		}
		programDAO.save(program);
	}

	
	/**
	 * Add elective courses given by array of IDs to the given program (defined by program ID).
	 * Checks: this method should not add elective course if 
	 * 1. it is already there (electives set);
	 * 2. it is in the program's core courses set.
	 * @param programID - given program ID
	 * @param courseIDs - array of IDs of courses to add to elective courses set
	 */
	public void addElectiveCourses(Long programID, Long[] courseIDs) {
		// TODO Do all business checks and throw exception if something wrong
		Program program = programDAO.getProgramById(programID);
		Set<Course> electiveCourses = program.getElectiveCourses();
		Set<Course> coreCourses = program.getCoreCourses();
		
		boolean found;
		Course courseToAdd;
		for (Long currentID: courseIDs) {
			found = false; // current course is found in core or electives
			
			for (Course course: electiveCourses) {
				if (course.getId().equals(currentID)) {
					found = true;
					break; // exit inner loop
				}
			}
			if (!found) {
				for (Course course: coreCourses) {
					if (course.getId().equals(currentID)) {
						found = true;
						break; // exit inner loop
					}
				}
			}
			
			if (found == false) {
				courseToAdd = courseDAO.getCourseById(currentID);
				electiveCourses.add(courseToAdd);
			}
		}
		
		programDAO.save(program);
	}
}