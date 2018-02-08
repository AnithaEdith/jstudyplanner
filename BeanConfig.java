package com.jstudyplanner;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;

/*


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
*/ /*
@Configuration
public class BeanConfig {

   @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
        SessionFactoryImplementor sessionFactory = hemf.getSessionFactory();
        return sessionFactory;
    }


   @Bean


}
*/
/*

    @Bean
    HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }

    @Bean
    @Qualifier(value = "entityManagerFactory")
    private static LocalContainerEntityManagerFactoryBean buildEntityManagerFactoryBean(DataSource dataSource, HibernateJpaVendorAdapter jpaVendorAdapter, String unitName) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName( "entityManagerFactory" );
        factoryBean.setPackagesToScan(BeanConfig.class.getPackage().getName());
        factoryBean.setJpaProperties( getJpaProperties() );

        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.afterPropertiesSet();

        // factoryBean.setPersistenceUnitName(unitName);
        return factoryBean;
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

        return properties;
    }

    @Bean
    @Qualifier(value = "sessionFactory")
    public FactoryBean<SessionFactory> sessionFactory(EntityManagerFactory entityManagerFactory )
    {
        HibernateJpaSessionFactoryBean hibernateJpaSessionFactoryBean = new HibernateJpaSessionFactoryBean();
        hibernateJpaSessionFactoryBean.setEntityManagerFactory( entityManagerFactory );
        return hibernateJpaSessionFactoryBean;
    }



    private static DataSource buildDataSource(String url, String username) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        return createConnectionPool(dataSource);
    }

    private static DataSource createConnectionPool(DataSource dataSource) {
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        return new HikariDataSource(config);
    }

    @Value("${spring.datasource.url}") String url;
    @Value("${spring.datasource.username}") String username;

    @Bean
    public DataSource moviesDataSource() {
        return buildDataSource(url, username);
    }

*/
/*
    @Configuration
    public static class Movies {
        @Value("${spring.datasource.url}") String url;
        @Value("${spring.datasource.username}") String username;

        @Bean
        public DataSource moviesDataSource() {
            return buildDataSource(url, username);
        }


        @Bean
        @Qualifier(value = "sessionFactory")
        public FactoryBean<SessionFactory> sessionFactory(EntityManagerFactory entityManagerFactory )
        {
            HibernateJpaSessionFactoryBean hibernateJpaSessionFactoryBean = new HibernateJpaSessionFactoryBean();
            hibernateJpaSessionFactoryBean.setEntityManagerFactory( entityManagerFactory );
            return hibernateJpaSessionFactoryBean;
        }


        @Bean
        @Qualifier(value = "entityManagerFactory")
        LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter jpaVendorAdapter) {
            return buildEntityManagerFactoryBean(moviesDataSource, jpaVendorAdapter, "jstudyplanner");
        }

      *//*
*/
/*  @Bean
        PlatformTransactionManager moviesTransactionManager(@Qualifier("jstudyplanner") LocalContainerEntityManagerFactoryBean factoryBean) {
            return new JpaTransactionManager(factoryBean.getObject());
        }*//*
*/
/*
    }*//*

*/
/*
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public SessionFactory getSessionFactory() {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }*//*


}*/
