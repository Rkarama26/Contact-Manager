package com.springboot.ContactManager.myconfig;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
	  http
	  
	    .authorizeHttpRequests(auth -> auth
	    		
	    		.requestMatchers("/admin/**")
	    		.hasRole("ADMIN")
	    		.requestMatchers("/user/**")
	    		.hasRole("USER")
	    		.requestMatchers("/**")
	    		.permitAll().anyRequest()
	    		.authenticated())
	    
	    
	    .httpBasic(withDefaults())
	    .formLogin(form -> form.loginPage("/login")
	    		               .loginProcessingUrl("/dologin")
	    		               .defaultSuccessUrl("/user/dashboard")
	    		              
	    		    )
	    .csrf(AbstractHttpConfigurer::disable);
	return http.build();
	}
	
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
}

