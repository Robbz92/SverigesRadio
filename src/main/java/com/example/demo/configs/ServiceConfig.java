package com.example.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ServiceConfig extends WebSecurityConfigurerAdapter {
    /*
        Detta är vår config/Service
     */
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected  void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/rest/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/rest/**").authenticated()
                .and()
                .formLogin()
                //.loginPage("/login") // vår egna login sida
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(myUserDetailsService.getEncoder());
    }

    // om vi använder ett costum login..
    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}