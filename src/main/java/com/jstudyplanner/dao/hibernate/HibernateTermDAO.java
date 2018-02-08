package com.jstudyplanner.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jstudyplanner.dao.TermDAO;
import com.jstudyplanner.domain.Term;

/**
 * Data Access Object that implements TermDAO interface using Hibernate 
 * to persist/update/delete/get Term objects into the database.
 * 
 * All methods apart from setSessionFactory should be executed within
 * transaction. get* methods correspond to readOnly transactions.
 * @author oleg
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
@Repository
public class HibernateTermDAO implements TermDAO {

	// injection should be defined in the hibernate-context.xml
	//@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	@Transactional(readOnly=false)
	public void add(Term term) {
		if (term.getEnabled() == null) {
			term.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().persist(term);
	}

	
	@Transactional(readOnly=false)
	public void save(Term term) {
		if (term.getEnabled() == null) {
			term.setEnabled((byte) 0);
		}
		this.sessionFactory.getCurrentSession().update(term);
	}

	
	@Transactional(readOnly=false)
	public void delete(Term term) {
		this.sessionFactory.getCurrentSession().delete(term);
	}

	
	/**
	 * Attempts to delete term found by given id
	 */
	@Transactional(readOnly=false)
	public void delete(Long id) {
		Term term = (Term) this.sessionFactory.getCurrentSession().get(Term.class, id);
		if (term != null) {
			this.sessionFactory.getCurrentSession().delete(term);
		}
	}

	
	public Term getTermById(Long id) {
		return (Term) this.sessionFactory.getCurrentSession().get(Term.class, id);
	}

	
	public Term getTermByYearAndNumber(Integer year, Integer number) {
		String hql = "FROM Term t WHERE t.year = :year AND t.number = :number ORDER BY t.year, t.number";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("number", number);
		List<Term> results = query.list();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	
	public int countAll() {
		String hql = "SELECT COUNT(*) FROM Term";
		return ((Long)this.sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}

	
	public List<Term> getAllTerms() {
		String hql = "FROM Term t ORDER BY t.year, t.number";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	
	public List<Term> getTermsByStatus(boolean enabled) {
		// TODO add unit test for getAllTermsByStatus()
		String hql = "FROM Term t WHERE t.enabled = :enabledCode ORDER BY t.year, t.number";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		byte enabledCode = enabled? (byte) 1 : (byte) 0;
		query.setParameter("enabledCode", enabledCode);
		return query.list();
	}

}