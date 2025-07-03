package com.example.gros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;

@Configuration 
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
            		 .requestMatchers("/api/users/register", "/api/users/login", "/h2-console/**").permitAll()
            		 .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()

            	     .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("ADMIN")
            		 .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ADMIN")
            		 .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("ADMIN")
            		 .anyRequest().authenticated()
            		)
   
            

            .headers(headers -> headers.frameOptions(frame -> frame.disable())) 
            .formLogin(form -> form.disable())
            .httpBasic(Customizer.withDefaults());

        return http.build();
	
 
	}
}