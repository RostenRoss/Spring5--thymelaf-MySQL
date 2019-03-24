package com.bolsadeideas.springboot.jpa.app.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	private HttpSession session;
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,@RequestParam(value="logout", required=false) String logout,Model model, Principal principal, RedirectAttributes flash) {
		if(principal!= null) {
			flash.addFlashAttribute("info","Ya has iniciado sesión!!");
			return "redirect:/";
		}
		if(error!=null) {
			model.addAttribute("error","Error en el loguin: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}
		if(logout!=null) {
			model.addAttribute("success","Ha cerrado sessión con éxito!");
		}
		return "login";
	}
	
	@GetMapping("/api/token")
	public String getToken() {
		
		return (String) session.getAttribute("token");
	}
}
