package pl.kurs.java.hateoas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DoctorsAppSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
			http//
					.httpBasic()//
					.and()//
					.authorizeRequests()//
					.antMatchers(HttpMethod.POST, "/doctor/uploadFile/**").authenticated()
					.antMatchers(HttpMethod.DELETE, "/doctor/").authenticated()//
					.anyRequest().permitAll()//
					.and()//
					.csrf().disable()//
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//
	}
}

