package com.uber.uberapi.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class UberAPISecurityConfig extends WebSecurityConfigurerAdapter {
    @Configuration
    @Order(1)
    public static class APISecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/passenger/**").hasAnyRole("PASSENGER", "ADMIN")
                    .antMatchers("/driver/**").hasAnyRole("DRIVER", "ADMIN")
                    .antMatchers("/location/**").hasAnyRole("DRIVER", "ADMIN", "PASSENGER") // loggedin and with proper roles
                    .and().apply(new JwtConfigurer(this.tokenProvider))
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    @Order(2)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Configuration
        public static class WebSecurityAdapter extends WebSecurityConfigurerAdapter {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().antMatchers("/").permitAll()
                        .antMatchers("/register/**").permitAll()
                        .antMatchers("/db-init/**").permitAll()
                        .antMatchers("/login/**").permitAll()
                        .antMatchers("/logout/**").permitAll()
                        .and().formLogin()
                        .and().csrf();
            }
        }
    }
