package com.jstudyplanner;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import javax.inject.Qualifier;
import javax.persistence.EntityManagerFactory;


/*        (exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})*/
//@ComponentScan
       // (basePackages = {"com.jstudyplanner.web", "com.jstudyplanner.service", "com.jstudyplanner.dao", "com.jstudyplanner"})
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        WebSecurityConfigurerAdapter.class
})
@EnableAutoConfiguration
public class Application {

 /*   @Bean
    public ServletRegistrationBean servletbean(DispatcherServlet dispatcherServlet){
        ServletRegistrationBean servletRegistrationBean= new ServletRegistrationBean(dispatcherServlet, "/*");
        servletRegistrationBean.setOrder(1);
        return servletRegistrationBean;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
