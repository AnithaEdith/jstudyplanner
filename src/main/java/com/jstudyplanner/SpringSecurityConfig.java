
package com.jstudyplanner;

import com.jstudyplanner.web.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SimpleUrlAuthenticationSuccessHandler successHandler;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/login")).and().authorizeRequests()
                .antMatchers("/admin/adminlist").hasRole("ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/staff/**").hasRole("STAFF")
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/login").permitAll()
                .antMatchers("/**").permitAll()
                .and().formLogin().successHandler(successHandler).failureUrl("/loginPage?error").loginProcessingUrl("/login").
                loginPage("/login").usernameParameter("username").passwordParameter("password").and().logout().permitAll();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT username, password, enabled FROM user WHERE username=?")
                .authoritiesByUsernameQuery(
                        "SELECT username, \n" +
                                "\t\t\t\t\tCASE WHEN username = 'admin' THEN 'ROLE_SUPERADMIN'\n" +
                                "\t\t\t\t\t\tWHEN utype = 'admin' THEN 'ROLE_ADMIN'\n" +
                                "\t\t\t\t\t\tWHEN utype = 'staff' THEN 'ROLE_STAFF'\n" +
                                "\t\t\t\t\t\tWHEN utype = 'student' THEN 'ROLE_STUDENT'\n" +
                                "\t\t\t\t\tEND AS role\n" +
                                "\t\t\t\t\tFROM user WHERE username=?");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("staff").password("staff").roles("STAFF");
        auth.inMemoryAuthentication().withUser("student").password("student").roles("STUDENT");
    }

}
