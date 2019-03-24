package com.bolsadeideas.springboot.jpa.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.jpa.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.jpa.app.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	JpaUserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private LoginSuccesHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/","/css/**","/js/**","/img/**","/listar","/locale", "/login").permitAll()
		.anyRequest().authenticated()//para trabajar con jwt se desabilita la pagina de login y logout
		.and()
		 .formLogin()
				.successHandler(successHandler)
				.loginPage("/login")
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403")
		/////////////////}
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);	}

	
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		////////////////autenticacion por jpa
		build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		
		
		
//		//////autenticacion por jdbc
//		build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder).usersByUsernameQuery("select username, password, enabled from users where username=?")
//		.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");


	}
}
