package com.jstudyplanner.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jstudyplanner.dao.MajorDAO;
import com.jstudyplanner.domain.Major;
import com.jstudyplanner.domain.Program;

/**
 * Data Access Object that implements MajorDAO interface using Hibernate 
 * to persist/update/delete/get Major objects into the database.
 * 
 * All methods apart from setSessionFactory should be executed within
 * transaction. get* methods correspond to readOnly transactions.
 * @author oleg
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
@Repository
public class HibernateMajorDAO implements MajorDAO {
	//@Autowired
	private SessionFactory sessionFactory;

	// injection should be defined in the hibernate-context.xml
//	private SessionFactory sessionFactory;

	@Autowired
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	@Transactional(readOnly=false)
	public void add(Major major) {
		if (major.getEnabled() == null) {
			major.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().persist(major);
	}

	
	@Transactional(readOnly=false)
	public void save(Major major) {
		if (major.getEnabled() == null) {
			major.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().update(major);
	}

	
	@Transactional(readOnly=false)
	public void delete(Major major) {
		this.sessionFactory.getCurrentSession().delete(major);
	}

	
	/**
	 * Attempts to delete major found by given id
	 */
	@Transactional(readOnly=false)
	public void delete(Long id) {
		Major major = (Major) this.sessionFactory.getCurrentSession().get(Major.class, id);
		if (major != null) {
			this.sessionFactory.getCurrentSession().delete(major);
		}
	}

	
	public Major getMajorById(Long id) {
		return (Major) this.sessionFactory.getCurrentSession().get(Major.class, id);
	}

	
	public Major getMajorByCode(String code) {
		String hql = "FROM Major m WHERE m.code = :code";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("code", code);
		List<Major> results = query.list();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * Get a major identified by title and program. 
	 * This method should be used to check major uniqueness.
	 */
	public Major getMajorByTitleAndProgram(String title, Program program) {
		// TODO write test methods
		String hql = "FROM Major m WHERE m.title = :title AND m.program = :program";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("title", title);
		query.setParameter("program", program);
		List<Major> results = query.list();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	
	public int countAll() {
		String hql = "SELECT COUNT(*) FROM Major";
		return ((Long)this.sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}

	
	public List<Major> getAllMajors() {
		String hql = "FROM Major m ORDER BY m.code";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
}