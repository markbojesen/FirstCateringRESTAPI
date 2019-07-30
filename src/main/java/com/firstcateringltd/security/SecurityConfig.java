//package com.firstcateringltd.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableAutoConfiguration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//    @Autowired
//    public DataSource dataSource;
//
//    @Autowired
//    private BasicAuthenticationPoint basicAuthenticationPoint;
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username,password, enabled from user where username=?")
//                .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/resources/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .csrf()
//            .disable()
//            .authorizeRequests()
//            .anyRequest()
//            .anonymous()
//            .and()
//            .httpBasic()
//            .disable();
//    }
//}