//Para representar as funções/perfis no sistema, a classe Role deve implementar a interface GrantedAuthority
//Como role será armazenado no banco, devemos mapea-lo no banco devidamente.

package br.com.fiap.Spare.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
public class Role implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	
	
	//Este método retorna a String que representa a autoridade/perfil/função
	@Override
	public String getAuthority() {
		return this.name;
	}
	
	//Método criado para retornar somente o nome da role, sem o ROLE_
	public String getRoleName() {
		return name.substring(5);
	}

}