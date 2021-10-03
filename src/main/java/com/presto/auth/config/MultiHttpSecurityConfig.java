package com.presto.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableWebSecurity
@EnableOAuth2Client
public class MultiHttpSecurityConfig {

    @Configuration
    public static class GeneralSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("user_detail_service")
        private UserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            // @formatter:off
            http.authorizeRequests()
                    .antMatchers("/oauth/token/revokeById/**").permitAll()
                    .antMatchers("/tokens/**").permitAll()
                    .anyRequest().authenticated()
                    .and().csrf().disable();
            // @formatter:on
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/actuator/**")
                    .antMatchers("/api_doc/**", "/doc/**", "/configuration/**", "/swagger*/**", "/webjars/**");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }

}