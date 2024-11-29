package com.nt.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
		// For plaintext (not recommended):
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf().disable()
    	
    		.authorizeHttpRequests((authorize)->{

    			authorize.requestMatchers("/employee/addEmployee").permitAll();  //it can access any one not reccomanded
    			authorize.requestMatchers("/employee/home").hasAnyAuthority("EMPLOYEE","MANAGER");
    			authorize.requestMatchers("/employee/getUser", "/employee/home").hasAuthority("EMPLOYEE");
    			//authorize.requestMatchers("/employee/getUser", "/employee/home").hasRole("EMPLOYEE");
    			authorize.requestMatchers("/employee/creatUser").hasAuthority("MANAGER");//need to write full path not end path

    			
    			//authorize.requestMatchers("/employee/**").authenticated();
    			//authorize.anyRequest().authenticated();

    			
    		})
    		.formLogin((form)-> form
    						.defaultSuccessUrl("/employee/home", true)
    						.permitAll()
        )
    	        .logout((logout) -> logout
    	            .logoutUrl("/logout") // Custom logout endpoint
    	            .logoutSuccessUrl("/login") // Redirect after successful logout
    	            .permitAll()
    	        )
    		.httpBasic(Customizer.withDefaults());
    	
    	return http.build();
    }
    
    
   @Bean
   public AuthenticationManager authenticationManageer(AuthenticationConfiguration configuration) throws Exception{
	   return configuration.getAuthenticationManager();
	   /*
	    This configuration is typically used in Spring Security to set up a custom AuthenticationManager,
	     which is responsible for handling authentication logic. For example, 
	     it could be used to integrate custom authentication providers or to use predefined authentication mechanisms.
	     
	     =>When a user tries to log in, Spring Security invokes the AuthenticationManager.
			The AuthenticationManager checks the credentials by calling the loadUserByUsername method of the UserDetailsService.
			Your loadUserByUsername implementation fetches user details from the database (via empRepo) and constructs a User object with roles (authorities).
			Spring Security then compares the provided password with the one retrieved from your UserDetails implementation (emp.getPassword() in your case).
	    */
   }
    
}
