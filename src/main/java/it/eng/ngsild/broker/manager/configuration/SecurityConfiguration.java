package it.eng.ngsild.broker.manager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService uds;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/admin").hasRole("ADMIN")
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/").permitAll()
		.and().formLogin()
		.loginPage("/login")
		// CSRF disabled for stateless REST API endpoints (server-to-server calls from Idra).
		// CSRF only applies to browser-based sessions; machine-to-machine POST calls have no
		// way to obtain and include a CSRF token from the cookie.
		.and().csrf().disable();

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(uds);
	}

}
