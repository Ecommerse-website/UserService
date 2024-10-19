package com.userService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.userService.filter.JwtAuthenticationEntryPoint;
import com.userService.filter.JwtAuthenticationFilter;


@Configuration
public class AuthConfig {
	
	@Autowired
	private JwtAuthenticationEntryPoint point;
	
	@Autowired
	private JwtAuthenticationFilter filter;

//	@Bean
//    public UserDetailsService userDetailsService(){
//        return new CustomUserDetailsService();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(requests -> requests
			.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/users/**").permitAll()
//			.requestMatchers("/users/**").authenticated()
			.anyRequest().authenticated())
		.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
		.sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.logout(logout -> logout.permitAll())
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
		.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception
	{
		return builder.getAuthenticationManager();
	}
}
