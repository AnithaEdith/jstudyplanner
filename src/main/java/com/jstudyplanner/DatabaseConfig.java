package com.jstudyplanner;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${db.driver}")
    private String DB_DRIVER;

    @Value("${db.url}")
    private String DB_URL;

    @Value("${db.username}")
    private String DB_USERNAME;

    @Value("${hibernate.dialect}")
    private String HIBERNATE_DIALECT;

    @Value("${hibernate.show_sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${hibernate.hbm2ddl.auto}")
    private String HIBERNATE_HBM2DDL_AUTO;

            @Bean
            public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(DB_DRIVER);
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            return dataSource;
            }


            @Bean
            public LocalSessionFactoryBean sessionFactory() {



                LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource());

            sessionFactoryBean.setMappingResources("mapping.hbm.xml");

            Properties hibernateProperties = new Properties();
            hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
            hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
            hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
            sessionFactoryBean.setHibernateProperties(hibernateProperties);

             return sessionFactoryBean;
}

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }


}

/*
package com.jstudyplanner;

import java.util.Properties;

import javax.sql.DataSource;

import com.jstudyplanner.dao.hibernate.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

@Configuration
public class DatabaseConfig {

    @Autowired
    private SessionFactory sessionFactory;

*/
/*  @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LocalSessionFactoryBean localSessionFactoryBean;


    // ------------------------
    // PUBLIC METHODS
    // ------------------------

*
     * DataSource definition for database connection. Settings are read from
     * the application.properties file (using the env object).


  @Bean
    HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }



    private DataSource buildDataSource(String url, String username) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        return (dataSource);
    }

  private static DataSource createConnectionPool(DataSource dataSource) {
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        return new HikariDataSource(config);
    }


    @Value("${jstudy.datasources.jst.url}") String url;
    @Value("${jstudy.datasources.jst.username}") String username;
    @Bean
    public DataSource dataSource() {
        return buildDataSource(url, username);
    }

*
     * Declare the JPA entity manager factory.


  @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource());

        // Classpath scanning of @Component, @Service, etc annotated class
        entityManagerFactory.setPackagesToScan("com.jstudyplanner.dao");

        // Vendor adapter
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        entityManagerFactory.setJpaProperties(getJpaProperties());

        return entityManagerFactory;
    }


    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean(){
        LocalSessionFactoryBean localSessionFactoryBean=new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setMappingResources("mapping.hbm.xml");
        localSessionFactoryBean.setHibernateProperties(getJpaProperties());
        return localSessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(localSessionFactoryBean().getObject());
        return transactionManager;
    }*//*


    @Bean
    public HibernateCampusDAO getCampusDao(){
        HibernateCampusDAO hibernateCampusDAO=new HibernateCampusDAO();
        hibernateCampusDAO.setSessionFactory(sessionFactory);
        return hibernateCampusDAO;

    }


    @Bean
    public HibernateCourseAvailabilityDAO getCourseAvailDao(){
        HibernateCourseAvailabilityDAO hibernateCourseAvailabilityDAO=new HibernateCourseAvailabilityDAO();
        hibernateCourseAvailabilityDAO.setSessionFactory(sessionFactory);
        return hibernateCourseAvailabilityDAO;

    }

    @Bean
    public HibernateCourseDAO getCourseDao(){
        HibernateCourseDAO hibernateCourseDAO=new HibernateCourseDAO();
        hibernateCourseDAO.se
        hibernateCourseDAO.setSessionFactory(sessionFactory);
        return hibernateCourseDAO;

    }

    @Bean
    public HibernateEnrollmentDAO getEnrollmentDao(){
        HibernateEnrollmentDAO hibernateEnrollmentDAO=new HibernateEnrollmentDAO();
        hibernateEnrollmentDAO.setSessionFactory(sessionFactory);
        return hibernateEnrollmentDAO;

    }


    @Bean
    public HibernateMajorDAO getMajorDao(){
        HibernateMajorDAO hibernateMajorDAO=new HibernateMajorDAO();
        hibernateMajorDAO.setSessionFactory(localSessionFactoryBean().getObject());
        return hibernateMajorDAO;

    }

    @Bean
    public HibernateProgramDAO getProgramDao(){
        HibernateProgramDAO hibernateProgramDAO=new HibernateProgramDAO();
        hibernateProgramDAO.setSessionFactory(localSessionFactoryBean().getObject());
        return hibernateProgramDAO;

    }

    @Bean
    public HibernateTermDAO gettermDao(){
        HibernateTermDAO hibernateTermDAO=new HibernateTermDAO();
        hibernateTermDAO.setSessionFactory(localSessionFactoryBean().getObject());
        return hibernateTermDAO;

    }

    @Bean
    public HibernateUserDAO gethibernateuserDao(){
        HibernateUserDAO hibernateUserDAO=new HibernateUserDAO();
        hibernateUserDAO.setSessionFactory(localSessionFactoryBean().getObject());
        return hibernateUserDAO;
    }

    private static Properties getJpaProperties()
    {
        Properties properties = new Properties();
        properties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.transaction.flush_before_completion", "true");
        properties.put("hibernate.transaction.auto_close_session", "false");
        properties.put("hibernate.use_outer_join", "true");

        properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        properties.put("hibernate.cache.use_second_level_cache", "true");
        properties.put("net.sf.ehcache.configurationResourceName", "META-INF/resources/ehcache-hibernate.xml");
        properties.put("hibernate.cache.use_query_cache", "true");
        properties.put("hibernate.jdbc.batch_size", "100");

        properties.put("hibernate.generate_statistics", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("org.hibernate.SQL", "info");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.show_sql", "true");

        return properties;
    }

*/
/*


*
     * Declare the transaction manager.


  @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager =
                new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory.getObject());
        return transactionManager;
    }


*
     * PersistenceExceptionTranslationPostProcessor is a bean post processor
     * which adds an advisor to any bean annotated with Repository so that any
     * platform-specific exceptions are caught and then rethrown as one
     * Spring's unchecked data access exceptions (i.e. a subclass of
     * DataAccessException).


  @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

*//*



} // class DatabaseConfig
*/
