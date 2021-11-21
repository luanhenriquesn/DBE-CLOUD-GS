//Esta é uma classe de configuração do Spring Security no nosso projeto, deixando nossas configurações personalizadas invés das que vem padrão
package br.com.fiap.Spare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.fiap.Spare.service.AuthenticationService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	//Recebendo o nosso UserDetailService personalizado por injeção de dependencia
	@Autowired
	private AuthenticationService authenticationService;

	
	//Autenticação
	//passwordEncoder() = Estamos utilizando o BCrypt, que é uma criptografia atual e muito boa, para criptografar a senha, e para dar certo, devemos deixar a senha do banco de dados criptografada tambem.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//Aqui estamos definindo qual será o UserDetailService utilizado para OBTER OS DADOS DO USUÁRIO E REALIZAR A AUTENTICAÇÃO, que no noss ocaso, é o que criamos e personalizamos.
		auth.userDetailsService(authenticationService)
			.passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	
	//Autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		
		http.authorizeRequests()
			.antMatchers("/user", "/user/delete/**")
			.hasRole("ADMIN")
			.antMatchers("/tip/**")
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/tip")
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout") )
			.logoutSuccessUrl("/");
			
		
	}
	
}
