package com.bootcamp.project.eCommerce.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

    @Autowired
    UserDetailsService jwtUserDetailsService;

    @Autowired
    CustomRequestFilter customRequestFilter;

    final static String ROLE_CUSTOMER = "CUSTOMER";
    final static String ROLE_SELLER = "SELLER";
    final static String ROLE_ADMIN = "ADMIN";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()

                .antMatchers("/register/*",
                        "/session/login",
                        "/session/reset/password/link").anonymous()

                .antMatchers("/session/admin/*").hasRole(ROLE_ADMIN)

                .antMatchers("/session/seller",
                        "/session/seller/*").hasRole(ROLE_SELLER)

                .antMatchers("/session/customer",
                        "/session/customer/*").hasRole(ROLE_CUSTOMER)

                .antMatchers("/session/logout",
                        "/session/reset/password").hasAnyRole(ROLE_ADMIN,
                        ROLE_CUSTOMER,
                        ROLE_SELLER)

                .and().exceptionHandling()
                .authenticationEntryPoint(tokenAuthenticationEntryPoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(customRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}