package com.jstudyplanner.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jstudyplanner.dao.CampusDAO;
import com.jstudyplanner.domain.Campus;

@RunWith(MockitoJUnitRunner.class)
public class HibernateCampusDAOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(HibernateCampusDAOTest.class);
	private static CampusDAO campusDAO;
	private static ApplicationContext beansCtx;

/*
	@Mock
	private static SessionFactory sessionFactory;
*/

	@BeforeClass
	public static void setUp() {
	//	MockitoAnnotations.initMocks(this);
		beansCtx = new ClassPathXmlApplicationContext("test-context.xml");
		campusDAO = (CampusDAO) beansCtx.getBean("campusDAO");

	}
	
	@Test
	public void testSetAndGetCampusDAO() {
		assertNotNull(campusDAO);
	}
	
	/**
	 * DAO should not add object with defined id because id should be assigned
	 * by the database (auto-increment).
	 * This test persists campus object into the database, retrieves it back, compares,
	 * deletes it from the database and checks the result.
	 * Methods tested:
	 * add(Campus)
	 * getCampusByCode(String)
	 * delete(Campus)
	 * delete(Long)
	 */
	@Test
	public void testAddGetDeleteCampus() {
	//	when(campusDAO.).thenReturn(sessionFactory);
		Campus ipswich = (Campus) beansCtx.getBean("ipswich");
		try {
			campusDAO.add(ipswich);
			fail();
		} catch (RuntimeException ex) {
			logger.info("campusDAO should not persist campus with id");
		}
		
		int numberOfCampuses = campusDAO.countAll();
		campusDAO.delete(16l);
		ipswich.setId(null);
		String ipswichCode = ipswich.getCode();
		campusDAO.add(ipswich);
		Campus ipswichFromDB = campusDAO.getCampusByCode(ipswichCode);
		assertNotNull(ipswichFromDB);
		assertTrue(ipswich.getTitle().equals(ipswichFromDB.getTitle()));
		assertTrue(campusDAO.countAll() == numberOfCampuses + 1);
		assertTrue(campusDAO.getAllCampuses().size() == numberOfCampuses + 1);
		campusDAO.delete(ipswichFromDB);
		ipswichFromDB = campusDAO.getCampusByCode(ipswichCode);
		assertNull(ipswichFromDB); // campus should be deleted
	}
	
	@Test
	public void testSaveCampus() {
		Campus ipswich = (Campus) beansCtx.getBean("ipswich");
		ipswich.setId(null);
		String ipswichCode = ipswich.getCode();
		campusDAO.add(ipswich);
		ipswich = campusDAO.getCampusByCode(ipswichCode);
		Long id = ipswich.getId();
		ipswich.setTitle("Ipswich Campus");
		campusDAO.save(ipswich);
		ipswich = campusDAO.getCampusById(id);
		assertTrue(ipswich.getTitle().equals("Ipswich Campus"));
		campusDAO.delete(ipswich);
		ipswich = campusDAO.getCampusById(id);
		assertNull(ipswich);
	}
}