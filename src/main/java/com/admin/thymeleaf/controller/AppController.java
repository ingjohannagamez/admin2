package com.admin.thymeleaf.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	@GetMapping({ "/", "/login"})
	public String index(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "index";
	}

	@GetMapping("/menu")
	public String menu() {
		return "menu";
	}

	@GetMapping("/user")
	public String user() {
		return "/user/user";
	}

	@GetMapping("/admin")
	public String admin() {
		return "/admin/admin";
	}

	@GetMapping
	public String error403() {
		return "/error/403";
	}

}
