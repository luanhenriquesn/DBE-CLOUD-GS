package br.com.fiap.Spare.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.Spare.exception.NotAllowedException;
import br.com.fiap.Spare.exception.TipNotFoundException;
import br.com.fiap.Spare.model.Tip;
import br.com.fiap.Spare.model.User;
import br.com.fiap.Spare.repository.TipRepository;

@Controller
@RequestMapping("/tip")
public class TipController {
	
	@Autowired
	private TipRepository repository;
	
	@Autowired
	private MessageSource message;
	
	
	
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("tips");
		
		List<Tip> tips = repository.findAll();
		modelAndView.addObject("tips", tips);
		
		return modelAndView;
	}
	
	
	@PostMapping
	public String save(@Valid Tip tip, BindingResult result, RedirectAttributes redirect, Authentication auth) {
		
		//Se houver errors, apenas volte para a página do formulário!
		if (result.hasErrors()) return "tip-form";
		
		User user = (User) auth.getPrincipal();
		tip.setUser(user)
		;
		repository.save(tip);
		
		//Adicionando mensagens no contexto da sessão, flash messages, para serem exibidas após o redirecionamento.
		redirect.addFlashAttribute("message", message.getMessage("tip.new.success", null, LocaleContextHolder.getLocale()));
		
		return "redirect:/tip/mytips";
	}
	
	
	@RequestMapping("new")
	public String create(Tip tip) {
		return "tip-form";
	}
	
	@RequestMapping("mytips")
	public ModelAndView myTips(Authentication auth) {
		
		User user = (User) auth.getPrincipal();
		
		//Buscar todas as dicas do usuario
		ArrayList<Tip> tips = new ArrayList<Tip>();
		List<Tip> allTips = repository.findAll();
		
		for (Tip tip : allTips) {
			if (tip.getUser().equals(user)) tips.add(tip);
		}
		
		ModelAndView modelAndView = new ModelAndView("my-tips");
		modelAndView.addObject("tips", tips);
		
		return modelAndView;
	}
	
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, Authentication auth) {
		
		//carregar a dica do BD
		Optional<Tip> optional = repository.findById(id);
		
		//VALIDAÇÃO PARA VERIFICA A EXISTENCIA DA DICA
		if (optional.isEmpty()) {
			throw new TipNotFoundException("Dica não encontrada");
		}
		
		Tip tip = optional.get();
		
		//Obter usuário que está logado/autenticado na sessão = GetPrincipal() retorna o objeto USUARIO.
		User user = (User) auth.getPrincipal();
		
		//Se não for o usuário dono da dica, ou se não for o usuário admin
		if ( !tip.getUser().equals(user) && !user.getRole().equalsIgnoreCase("ADMIN")) {
			throw new NotAllowedException("A dica é de outro usuário.");
		}
		
		//apagando do banco
		repository.delete(tip); 
		
		if (user.getRole().equalsIgnoreCase("ADMIN")) {
			return "redirect:/tip";
		}
		return "redirect:/tip/mytips";
		
	}
	
	
	@GetMapping("{id}")
	public ModelAndView update(@PathVariable Long id, Authentication auth) {
		
		//carregar a dica do BD
		Optional<Tip> optional = repository.findById(id);
		
		//VALIDAÇÃO PARA VERIFICA A EXISTENCIA DA DICA
		if (optional.isEmpty()) {
			throw new TipNotFoundException("Dica não encontrada");
		}
		
		
		Tip tip = optional.get();
		
		//Obter usuário que está logado/autenticado na sessão = GetPrincipal() retorna o objeto USUARIO.
		User user = (User) auth.getPrincipal();
		
		//Se não for o usuário dono da dica
		if ( !tip.getUser().equals(user)) {
			throw new NotAllowedException("A dica é de outro usuário.");
		}
		
		ModelAndView modelAndView = new ModelAndView("tip-update");
		modelAndView.addObject("tip", tip);
		
		return modelAndView;
	}
	
	
	@PostMapping("update")
	public String update(Tip tip, Authentication auth) {
		
		//Obter usuário que está logado/autenticado na sessão = GetPrincipal() retorna o objeto USUARIO.
		User user = (User) auth.getPrincipal();
		
		tip.setUser(user);
		
		//Se não for o usuário dono da dica
		if ( !tip.getUser().equals(user)) {
			throw new NotAllowedException("A dica é de outro usuário.");
		}
		
		repository.save(tip);
		
		return "redirect:/tip";
		
	}
	
}
