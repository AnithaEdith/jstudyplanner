package com.jstudyplanner.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jstudyplanner.dao.CourseAvailabilityDAO;
import com.jstudyplanner.domain.Campus;
import com.jstudyplanner.domain.Course;
import com.jstudyplanner.domain.CourseAvailability;
import com.jstudyplanner.domain.Term;

/**
 * Data Access Object that implements CourseAvailabilityDAO interface using Hibernate 
 * to persist/update/delete/get CourseAvailability objects into the database.
 * 
 * All methods apart from setSessionFactory should be executed within
 * transaction. get* methods correspond to readOnly transactions.
 * @author oleg
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
@Repository
public class HibernateCourseAvailabilityDAO implements CourseAvailabilityDAO {
	
	// injection should be defined in the hibernate-context.xml
	//@Qualifier("sessionFactory")
	//@Autowired
	private SessionFactory sessionFactory;



	@Autowired
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly=false)
	public void add(CourseAvailability courseAvailability) {
		if (courseAvailability.getEnabled() == null) {
			courseAvailability.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().persist(courseAvailability);
	}

	@Transactional(readOnly=false)
	public void save(CourseAvailability courseAvailability) {
		if (courseAvailability.getEnabled() == null) {
			courseAvailability.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().update(courseAvailability);
	}

	@Transactional(readOnly=false)
	public void delete(CourseAvailability courseAvailability) {
		this.sessionFactory.getCurrentSession().delete(courseAvailability);
	}
	
	/**
	 * Attempts to delete CourseAvailability found by given id
	 */
	@Transactional(readOnly=false)
	public void delete(Long id) {
		CourseAvailability courseAvailability = (CourseAvailability) this.sessionFactory.getCurrentSession().get(CourseAvailability.class, id);
		if (courseAvailability != null) {
			this.sessionFactory.getCurrentSession().delete(courseAvailability);
		}
	}
	
	
	public CourseAvailability getCAById(Long id) {
		return (CourseAvailability) this.sessionFactory.getCurrentSession().get(CourseAvailability.class, id);
	}
	
	
	public CourseAvailability getCAByTermCourseCampus(Term term, Course course, Campus campus) {
		String hql = "FROM CourseAvailability ca WHERE ca.term = :term AND ca.course = :course AND ca.campus = :campus";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("term", term);
		query.setParameter("course", course);
		query.setParameter("campus", campus);
		List<CourseAvailability> results = query.list();
		if (results.size() != 0) {
			CourseAvailability result = results.get(0);
			// initialize properties (related objects)
			Hibernate.initialize(result.getTerm());
			Hibernate.initialize(result.getCampus());
			Hibernate.initialize(result.getCourse());
			return result;
		} else {
			return null;
		}
	}
	
	
	public List<CourseAvailability> getAllCAs() {
		// TODO add unit test for getAllCAs()
		String hql = "FROM CourseAvailability ca ORDER BY ca.campus.title, ca.term.year, ca.term.number, ca.course.code";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	
	public List<CourseAvailability> getCAsByStatus(boolean enabled) {
		// TODO add unit test for getAllCAsByStatus()
		String hql = "FROM CourseAvailability ca WHERE ca.enabled = :enabledCode "
				+ "ORDER BY ca.campus.title, ca.term.year, ca.term.number, ca.course.code";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		byte enabledCode = enabled? (byte) 1 : (byte) 0;
		query.setParameter("enabledCode", enabledCode);
		return query.list();
	}
	
	
	public List<CourseAvailability> getCAByTerm(Term term) {
		String hql = "FROM CourseAvailability ca WHERE ca.term = :term";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("term", term);
		List<CourseAvailability> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
	
	
	public List<CourseAvailability> getCAByCourse(Course course) {
		String hql = "FROM CourseAvailability ca WHERE ca.course = :course";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("course", course);
		List<CourseAvailability> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
	
	
	public List<CourseAvailability> getCAByCampus(Campus campus) {
		String hql = "FROM CourseAvailability ca WHERE ca.campus = :campus";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("campus", campus);
		List<CourseAvailability> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
	
	
	public List<CourseAvailability> getCAByCampusAndTerm(Campus campus,	Term term) {
		String hql = "FROM CourseAvailability ca WHERE ca.campus = :campus AND ca.term = :term";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("campus", campus);
		query.setParameter("term", term);
		List<CourseAvailability> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
	
	
	public int countAll() {
		String hql = "SELECT COUNT(*) FROM CourseAvailability";
		return ((Long)this.sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}

}