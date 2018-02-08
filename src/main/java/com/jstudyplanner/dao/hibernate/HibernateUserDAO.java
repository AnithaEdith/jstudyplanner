package com.jstudyplanner.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jstudyplanner.dao.UserDAO;
import com.jstudyplanner.domain.Staff;
import com.jstudyplanner.domain.User;


/**
 * Data Access Object that implements UserDAO interface using Hibernate 
 * to persist/update/delete/get User objects into the database.
 * 
 * All methods apart from setSessionFactory should be executed within
 * transaction. get* methods correspond to readOnly transactions.
 * @author oleg
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
@Repository
public class HibernateUserDAO implements UserDAO {
	
	// injection should be defined in the hibernate-context.xml
//	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	/**
	 * Adds user. Set enabled status to 0 if it is not defined
	 */
	@Transactional(readOnly=false)
	public void add(User user) {
		if (user.getEnabled() == null) {
			user.setEnabled((byte) 0);
		}
	//	this.sessionFactory.getCurrentSession().persist(user);
		this.sessionFactory.getCurrentSession().persist(user);
	}

	
	/**
	 * Save (update) user. Set enabled status to 0 if it is not defined
	 */
	@Transactional(readOnly=false)
	public void save(User user) {
		if (user.getEnabled() == null) {
			user.setEnabled((byte) 0);
		}
//		this.sessionFactory.getCurrentSession().update(user);
		this.sessionFactory.getCurrentSession().update(user);

	}

	
	/**
	 * Deletes user. Before deleting check all dependencies in the service object
	 */
	@Transactional(readOnly=false)
	public void delete(User user) {

	//	this.sessionFactory.getCurrentSession().delete(user);
		this.sessionFactory.getCurrentSession().delete(user);

	}
	

	@Transactional(readOnly=false)
	public void delete(String username) {
		User user = getUserByUsername(username);
		if (user != null) {
	//		this.sessionFactory.getCurrentSession().delete(user);
			this.sessionFactory.getCurrentSession().delete(user);
		}
		// TODO else if not found (throw exception)
	}

	
	@Transactional(readOnly=false)
	public void delete(Long id) {
		User user = (User) this.sessionFactory.getCurrentSession().get(User.class, id);
		if (user != null) {
			this.sessionFactory.getCurrentSession().delete(user);
		}
	}

	
	public User getUserById(Long id) {
		return (User) this.sessionFactory.getCurrentSession().get(User.class, id);
	}

	
	public User getUserByUsername(String username) {
		String hql = "FROM User u WHERE u.username = :username";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("username", username);
		List<User> results = query.list();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	
	public User getUserByEmail(String email) {
		String hql = "FROM User u WHERE u.email = :email";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("email", email);
		List<User> results = query.list();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	
	public List<User> getAllUsers() {
		String hql = "FROM User u";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	
	public int countAll() {
		String hql = "SELECT COUNT(*) FROM User";
		return ((Long)this.sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}
	

	public <T> List<T> getUsersByType(String userType) {
		String hql = "FROM User u WHERE u.utype = :userType";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userType", userType);
		return query.list();
		// TODO write test methods
	}

	
	public int countByUserType(String userType) {
		String hql = "SELECT COUNT(*) FROM User u WHERE u.utype = :userType";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userType", userType);
		return ((Long)query.uniqueResult()).intValue();
		// TODO write test methods
	}
}