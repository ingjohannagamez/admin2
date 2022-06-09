package com.admin.thymeleaf.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.admin.thymeleaf.service.UserDetailsServiceImpl;



@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	DataSource dataSource;
	
	// Necesario para evitar que la seguridad se aplique a los resources
	// Como los css, imagenes y javascripts
	String[] resources = new String[] { "/include/**", "/css/**", "/icons/**", "/img/**", "/js/**", "/layer/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(resources).permitAll()
			.antMatchers("/", "/index").permitAll()
			.antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
			.antMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/menu")
				.failureUrl("/login?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
			.rememberMe()
				.rememberMeParameter("remember-me")
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(120)//tiempo de vida del token
				.userDetailsService(userDetailsService)
				.and()
			.logout()
				.permitAll()
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/login?logout")
				.and()
			.exceptionHandling()
                .accessDeniedPage("/403");
	}

	// Crea el encriptador de contraseñas
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
		//El numero 4 representa que tan fuerte quieres la encriptacion.
		//Se puede en un rango entre 4 y 31. 
		//Si no pones un numero el programa utilizara uno aleatoriamente cada vez
		//que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
		return bCryptPasswordEncoder;
	}

	// Registra el service para usuarios y el encriptador de contrasena
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}
	
}