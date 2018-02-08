package com.jstudyplanner.service.implementation;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.CampusDAO;
import com.jstudyplanner.dao.CourseAvailabilityDAO;
import com.jstudyplanner.dao.CourseDAO;
import com.jstudyplanner.dao.TermDAO;
import com.jstudyplanner.domain.Campus;
import com.jstudyplanner.domain.Course;
import com.jstudyplanner.domain.CourseAvailability;
import com.jstudyplanner.domain.Term;
import com.jstudyplanner.service.CAService;

@Component("caService")
public class CAServiceImpl implements CAService {

	/*@Autowired
	private SessionFactory sessionFactory;
*/

	@Autowired CourseAvailabilityDAO caDAO;
	@Autowired CourseDAO courseDAO;
	@Autowired CampusDAO campusDAO;
	@Autowired TermDAO termDAO;
	
	public List<CourseAvailability> getAllCAs() {
		return caDAO.getAllCAs();
	}

	
	public List<CourseAvailability> getCAByStatus(boolean enabled) {
		return caDAO.getCAsByStatus(enabled);
	}

	// TODO develop methods: getCAByCampus, getCAByTerm, getCAByCourse with enabled criteria
	
	
	public List<CourseAvailability> getCAByCampus(Campus campus) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		return caDAO.getCAByCampus(campus);
	}

	
	public List<CourseAvailability> getCAByTerm(Term term) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		return caDAO.getCAByTerm(term);
	}

	
	public List<CourseAvailability> getCAByCourse(Course course) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		return caDAO.getCAByCourse(course);
	}

	
	/**
	 * Attempts to save/update CourseAvailability.
	 * Check if combination of campus, term and course is unique. If not throw exception.
	 * Else save/update this term.
	 * If CourseAvailability doesn't have id then this is a new one - persist it.
	 * Else update existing CourseAvailability. 
	 */
	public void save(CourseAvailability ca) {
		// TODO add business checks for enrollments
		CourseAvailability caToCheck = caDAO.getCAByTermCourseCampus(ca.getTerm(), ca.getCourse(), ca.getCampus());
		if (GeneralService.domainObjectsAreSame(ca, caToCheck)) {
			throw new CustomServiceException("", "Course " + ca.getCourse().getTitle() + " has been already scheduled "
				+ " in campus " + ca.getCampus().getTitle() + " for term " + ca.getTerm().getNumber() + " "
				+ ca.getTerm().getYear() + ". Combination of course, campus, and term should be unique.");
		}
		
		if ( ca.getId() == null ) {
			caDAO.add(ca);
		} else {
			caDAO.save(ca);
		}
	}

	
	public void delete(CourseAvailability ca) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		// TODO check all business constraints for ca delete (e.g. enrollments)
		caDAO.delete(ca);
	}

	
	public CourseAvailability getCAById(Long id) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		return caDAO.getCAById(id);
	}

	
	public CourseAvailability getCAByCampusTermAndCourse(Campus campus,	Term term, Course course) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		return caDAO.getCAByTermCourseCampus(term, course, campus);
	}


	public List<CourseAvailability> getCAByCampusAndTerm(Campus campus, Term term) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		List<CourseAvailability> caList = caDAO.getCAByCampusAndTerm(campus, term);
		if (caList == null) {
			throw new CustomServiceException("", "There are no courses available in " + term.getShortDescription()
				+ ", " + campus.getTitle() + ".");
		} else return caList;
	}


	/**
	 * Get list of courses that can be used to create multiple CourseAvailability entities
	 * for given campus and term.
	 * That includes all enabled courses that are not scheduled for given campus and term yet.
	 * @param campusID - given campus ID
	 * @param termID - given term ID
	 */
	public List<Course> getAvailableCACourses(Long campusID, Long termID) {
		List<Course> result;
		result = courseDAO.getCoursesByStatus(true);
		
		Campus campus = campusDAO.getCampusById(campusID);
		Term term = termDAO.getTermById(termID);
		List<CourseAvailability> caList = caDAO.getCAByCampusAndTerm(campus, term);
		
		if (caList != null) {
			for (CourseAvailability ca : caList) {
				result.remove(ca.getCourse());
			}
		}
		
		return result;
	}


	/**
	 * Create multiple CourseAvailability objects by given Campus, Term, and 
	 * courses determined by array of course IDs. Returns message containing list of courses
	 * added to the schedule.
	 * @param campus - given campus
	 * @param term - given term
	 * @param courseIDs - array of course IDs
	 */
	public StringBuilder createMultipleCA(Campus campus, Term term, Long[] courseIDs) {
		StringBuilder infoMessage = new StringBuilder("Campus: " + campus.getTitle() + ", " 
				+ term.getShortDescription() + ". Courses added: ");
		
		Byte enabled = new Byte((byte) 1);
		boolean addedCourse = false;
		Course course;
		CourseAvailability ca;
		for (Long courseID : courseIDs) {
			course = courseDAO.getCourseById(courseID);
			ca = caDAO.getCAByTermCourseCampus(term, course, campus);
			if (ca == null) {
				ca = new CourseAvailability(null, term, course, campus, enabled);
				caDAO.add(ca);
				
				if (addedCourse) {
					infoMessage.append(", ");
				}
				infoMessage.append(course.getTitle());
				addedCourse = true;
			}
		}
		return infoMessage;
	}
	
}