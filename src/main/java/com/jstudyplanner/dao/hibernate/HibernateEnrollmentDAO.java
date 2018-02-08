package com.jstudyplanner.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jstudyplanner.dao.EnrollmentDAO;
import com.jstudyplanner.domain.CourseAvailability;
import com.jstudyplanner.domain.Enrollment;
import com.jstudyplanner.domain.Student;

/**
 * Data Access Object that implements EnrollmentDAO interface using Hibernate 
 * to persist/update/delete/get Enrollment objects into the database.
 * 
 * All methods apart from setSessionFactory should be executed within
 * transaction. get* methods correspond to readOnly transactions.
 * @author oleg
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
@Repository
public class HibernateEnrollmentDAO implements EnrollmentDAO {

	// injection should be defined in the hibernate-context.xml
	//@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void setSessionFactory(SessionFactory sessionFactory) {
		sessionFactory = sessionFactory;
	}


	
	@Transactional(readOnly=false)
	public void add(Enrollment enrollment) {
		this.sessionFactory.getCurrentSession().persist(enrollment);
	}

	
	@Transactional(readOnly=false)
	public void save(Enrollment enrollment) {
		this.sessionFactory.getCurrentSession().update(enrollment);
	}

	
	@Transactional(readOnly=false)
	public void delete(Enrollment enrollment) {
		this.sessionFactory.getCurrentSession().delete(enrollment);
	}

	
	/**
	 * Attempts to delete Enrollment found by given id
	 */
	@Transactional(readOnly=false)
	public void delete(Long id) {
		Enrollment enrollment = (Enrollment) this.sessionFactory.getCurrentSession().get(Enrollment.class, id);
		if (enrollment != null) {
			this.sessionFactory.getCurrentSession().delete(enrollment);
		}
	}

	
	public Enrollment getEnrollmentById(Long id) {
		return (Enrollment) this.sessionFactory.getCurrentSession().get(Enrollment.class, id);
	}

	
	public Enrollment getEnrollmentByCAAndStudent(CourseAvailability ca, Student student) {
		String hql = "FROM Enrollment e WHERE e.courseAvailability = :ca AND e.student = :student";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("ca", ca);
		query.setParameter("student", student);
		List<Enrollment> results = query.list();
		if (results.size() != 0) {
			Enrollment result = results.get(0);
			// initialize properties (related objects, lazy loading)
			Hibernate.initialize(result.getCourseAvailability());
			Hibernate.initialize(result.getStudent());
			return result;
		} else {
			return null;
		}
	}

	
	public List<Enrollment> getAllEnrollments() {
		String hql = "FROM Enrollment e";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Enrollment> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
	
	
	public List<Enrollment> getEnrollmentsByCA(CourseAvailability ca) {
		String hql = "FROM Enrollment e WHERE e.courseAvailability = :ca";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("ca", ca);
		List<Enrollment> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}

	
	public List<Enrollment> getEnrollmentsByStudent(Student student) {
		String hql = "FROM Enrollment e WHERE e.student = :student";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("student", student);
		List<Enrollment> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}

	
	public int countAll() {
		String hql = "SELECT COUNT(*) FROM Enrollment";
		return ((Long)this.sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}


	public List<Enrollment> getEnrollmentsByStatus(String status) {
		String hql = "FROM Enrollment e WHERE e.status = :status";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("status", status);
		List<Enrollment> results = query.list();
		if (results.size() != 0) {
			return results;
		} else {
			return null;
		}
	}
}