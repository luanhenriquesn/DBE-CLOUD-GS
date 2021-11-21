//Nesta classe Model, estamos implementando a classe UserDetails (Utilizada para autenticação do usuário), assim, a classe model também fica responsável por represenar os detalhes de um usuário autenticado no sistema.
//E com isso, precisamos implementar os métodos específicos de UserDetails, conseguindo Personalizar-los da nossa maneira para nossa aplicação.

package br.com.fiap.Spare.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Entity
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "{user.name.blank}")
	private String name;
	
	@NotBlank(message = "{user.email.blank}")
	@Email(message = "{user.email.invalid}")
	private String email;
	
	@Size(min = 8, message = "{user.password.size}")
	private String password;
	
	//Um usuário pode ter muitas funções, e uma função pode ser de muitos usuários.
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles;
	

	
	//Método usado para adicionar a role selecionada ao usuario no cadastro.
	public void addRole(Role role) {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		roles.add(role);
	}
	
	
	//Retornando o nome do perfil do usuário
	public String getRole() {
		for (Role role : roles) {
			return role.getName().substring(5);
		}
		return "";
	}
	
	
	
	
	//Métodos de UserDetails
	
	
	//Método que retorna uma coleção de perfis/autoridades/permissões que o usuário tem no sistema.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}
	
	
	//No caso, na nossa aplicação, o "username" seria o email, tendo em vista que o login é feito com o email.
	@Override
	public String getUsername() {
		return this.email;
	}

	
	
	//MÉTODOS PARA OBTER OS ESTADOS DO USUARIO
	
	//Método para saber se a conta não está expirada.
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	
	//Nossa aplicação não possui regras para bloquear contas, portanto, retorna true sempre.
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	
	//Este método é usado para verificar se as credenciais não expiraram.
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
	//Retorna se a conta está habilitada.
	@Override
	public boolean isEnabled() {
		return true;
	}


	//Devemos sobrescrever os métodos equalsAndHashCode, para fazer a comparação do objeto usuário apenas pelo ID, para não gerar problemas com a adição das roles no sistema.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	

}
