//AuthenticationService é o meu UserDetailService personalizado(Classe que pega os detalhes do usuário para fazer a autenticação)

package br.com.fiap.Spare.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.Spare.model.User;
import br.com.fiap.Spare.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	//Para fazer a autenticação dos usuários no banco de dados, precisamos acessar o banco de dados:
	@Autowired
	private UserRepository repository;
	
	
	
	//Sobrescrevendo o método da interface UserDetailService, para personalizarmos da nossa forma a autenticação do usuario, que será feita pela busca através do username.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//No caso, na nossa aplicação, o "username" seria o email, tendo em vista que o login é feito com o email.
		Optional<User> user = repository.findByEmail(username);
		
		//Se o usuário não for encontrado, lançamos uma exceção.
		if (user.isEmpty()) throw new UsernameNotFoundException("User not found");
		
		//Retornando o Model User, que é uma implementação da interface UserDetails
		return user.get();
		
	}
	
	
	
	
	
}
