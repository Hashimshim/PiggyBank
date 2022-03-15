package com.example.piggybank.security;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder1 = PasswordEncoderFactories.createDelegatingPasswordEncoder();

//        auth.inMemoryAuthentication().withUser("admin").password("{noop}123").roles("ADMIN","USER");
//        auth.inMemoryAuthentication().withUser("user").password("{noop}123").roles("USER");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal , password as credentials , active from users where username=?")
                .authoritiesByUsernameQuery("select username as principal , role as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(new MessageDigestPasswordEncoder("MD5"));
//               .passwordEncoder(NoOpPasswordEncoder.getInstance());
                //.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode());
//                .passwordEncoder(new BCryptPasswordEncoder().);
        
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login");
        http.authorizeRequests().antMatchers("/operations","/consulterCompte").hasRole("USER");
        http.authorizeRequests().antMatchers("/save").hasRole("ADMIN");
        http.exceptionHandling().accessDeniedPage("/403");


    }
}
