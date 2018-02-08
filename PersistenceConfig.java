//
//
//package com.jstudyplanner;
//
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
//import org.springframework.orm.hibernate4.HibernateTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//public class PersistenceConfig {
//
//
//    @Bean
//    public AnnotationSessionFactoryBean sessionFactory() {
//        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
//        sessionFactory.setDataSource(moviesDataSource());
//        sessionFactory.setPackagesToScan(PersistenceConfig.class.getPackage().getName());
//        sessionFactory.setHibernateProperties(getJpaProperties());
//
//        return sessionFactory;
//    }
//
//    private static Properties getJpaProperties()
//    {
//        Properties properties = new Properties();
//        properties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
//        properties.put("hibernate.hbm2ddl.auto", "validate");
//        properties.put("hibernate.transaction.flush_before_completion", "true");
//        properties.put("hibernate.transaction.auto_close_session", "false");
//        properties.put("hibernate.use_outer_join", "true");
//
//        properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
//        properties.put("hibernate.cache.use_second_level_cache", "true");
//        properties.put("net.sf.ehcache.configurationResourceName", "META-INF/resources/ehcache-hibernate.xml");
//        properties.put("hibernate.cache.use_query_cache", "true");
//        properties.put("hibernate.jdbc.batch_size", "100");
//
//        properties.put("hibernate.generate_statistics", "true");
//        properties.put("hibernate.format_sql", "true");
//        properties.put("hibernate.use_sql_comments", "true");
//        properties.put("org.hibernate.SQL", "info");
//
//        return properties;
//    }
//    private static DataSource buildDataSource(String url, String username) {
//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setURL(url);
//        dataSource.setUser(username);
//        return createConnectionPool(dataSource);
//    }
//    private static DataSource createConnectionPool(DataSource dataSource) {
//        HikariConfig config = new HikariConfig();
//        config.setDataSource(dataSource);
//        return new HikariDataSource(config);
//    }
//
//    @Value("${spring.datasource.url}") String url;
//    @Value("${spring.datasource.username}") String username;
//
//    @Bean
//    public DataSource moviesDataSource() {
//        return buildDataSource(url, username);
//    }
//
//
//    @Bean
//    @Autowired
//    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
//        HibernateTransactionManager txManager = new HibernateTransactionManager();
//        txManager.setSessionFactory(sessionFactory);
//
//        return txManager;
//    }
//
//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//}
