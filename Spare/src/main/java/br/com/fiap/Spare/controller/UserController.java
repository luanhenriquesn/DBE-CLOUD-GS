package br.com.fiap.Spare.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.Spare.exception.NotAllowedException;
import br.com.fiap.Spare.exception.UserNotFoundException;
import br.com.fiap.Spare.model.Role;
import br.com.fiap.Spare.model.Tip;
import br.com.fiap.Spare.model.User;
import br.com.fiap.Spare.repository.RoleRepository;
import br.com.fiap.Spare.repository.TipRepository;
import br.com.fiap.Spare.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private TipRepository tipRepository;
	
	
	@Autowired
	private MessageSource messages;
	
	
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("users");
		List<User> users = repository.findAll();
		modelAndView.addObject("users", users);
		return modelAndView;
	}
	
	
	@PostMapping("/new")
	public String save(@Valid User user, Long roleId, BindingResult result, RedirectAttributes redirect) {
		
		if(result.hasErrors()) return "user-form";
		
		Optional<Role> role = roleRepository.findById(roleId);
		
		
		//Antes de salvar o Usuário no banco, vamos criptografar sua senha com BCrypt, para assim ser compara com sucesso com a criptografia usada no nosso Spring.
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		//inserindo a role do usuario
		user.addRole(role.get());
		
		repository.save(user);
		redirect.addFlashAttribute("message", messages.getMessage("message.success.newuser", null, LocaleContextHolder.getLocale()));
		return "redirect:/login";
	}
	

	@RequestMapping("new")
	public ModelAndView create(User user) {
		ModelAndView modelAndView = new ModelAndView("user-form");
		List<Role> roles = roleRepository.findAll();
		modelAndView.addObject("roles", roles);
		return modelAndView;
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, Authentication auth) {
		
		//carregar a usuário do BD
		Optional<User> optional = repository.findById(id);
		
		//VALIDAÇÃO PARA VERIFICA A EXISTENCIA DO usuário
		if (optional.isEmpty()) {
			throw new UserNotFoundException("Usuário não encontrado");
		}
		
		User user = optional.get();
		
		//Obter usuário que está logado/autenticado na sessão = GetPrincipal() retorna o objeto USUARIO.
		User userLog = (User) auth.getPrincipal();
		
		//Se não for o usuário admin
		if ( !userLog.getRole().equalsIgnoreCase("ADMIN")) {
			throw new NotAllowedException("Você não tem acesso a essa funcionalidade.");
		}
		
		//APagando as tarefas relacionadas ao usuário
		List<Tip> tips = tipRepository.findAll();
		for (Tip tip : tips) {
			if (tip.getUser().getId() == user.getId()) tipRepository.delete(tip);
		}
		//apagando do banco
		repository.delete(user); 
		
		if (user.equals(userLog)) {
			return "redirect:/";
		}
		
		return "redirect:/user";
		
	}
	
	
	
}
