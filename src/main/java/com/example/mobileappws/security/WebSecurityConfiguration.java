package com.example.mobileappws.security;

import com.example.mobileappws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public WebSecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> {
                            try {
                                authz
                                        .mvcMatchers(HttpMethod.POST, SecurityConstant.SING_UP_URL).permitAll()
                                        .mvcMatchers(HttpMethod.GET, SecurityConstant.VERIFICATION_EMAIL_URL).permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilter(authenticationFilter(authenticationManager))
                                        .addFilter(new AuthorizationFilter(authenticationManager))
                                        .authenticationManager(authenticationManager)
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    private AuthenticationFilter authenticationFilter(AuthenticationManager manager) {
        final AuthenticationFilter filter = new AuthenticationFilter(manager);
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
