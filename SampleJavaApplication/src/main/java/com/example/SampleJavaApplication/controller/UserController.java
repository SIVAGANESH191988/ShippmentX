package com.example.SampleJavaApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SampleJavaApplication.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	  private final UserService userService;

	    public UserController(UserService userService) {
	        this.userService = userService;
	    }

	    @GetMapping("/login")
	    public String showLoginPage() {
	        return "login";
	    }

	    @PostMapping("/login")
	    public String login(@RequestParam String username, @RequestParam String password, Model model) {
	        if (userService.authenticate(username, password)) {
	            model.addAttribute("username", username);
	            return "welcome";
	        }
	        return "error";
	    }

	    @GetMapping("/register")
	    public String showRegisterPage() {
	        return "register";
	    }

	    @PostMapping("/register")
	    public String register(@RequestParam String username, @RequestParam String password, Model model) {
	        userService.register(username, password);
	        model.addAttribute("username", username);
	        return "welcome";
	    }
}
