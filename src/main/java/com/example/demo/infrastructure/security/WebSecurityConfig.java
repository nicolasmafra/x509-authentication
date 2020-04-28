package com.example.demo.infrastructure.security;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2Enabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
            .addFilter(x509AuthenticationFilter());

        if (h2Enabled) {
            http
                .csrf().disable()
                .headers()
                    .frameOptions().disable();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
            .authenticationProvider(customAuthenticationProvider());
    }

    private X509AuthenticationFilter x509AuthenticationFilter() throws Exception {
        X509AuthenticationFilter x509Filter = new X509AuthenticationFilter();
        x509Filter.setAuthenticationManager(authenticationManager());
        return x509Filter;
    }

    private CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userService::findByChavePublica);
    }

}
