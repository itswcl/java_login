package com.wei.loginregistration.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.wei.loginregistration.models.LoginUser;
import com.wei.loginregistration.models.User;
import com.wei.loginregistration.services.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;
	
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index.jsp";
    }
    
    @PostMapping("/register")
    public String register(
    		@Valid
    		@ModelAttribute("newUser") User newUser, 
            BindingResult result,
            Model model,
            HttpSession session) {
    	
    	userService.register(newUser, result);
    	
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }
        
        session.setAttribute("user_id", newUser.getId());
        return "redirect:/success";
    }
   
    
    @PostMapping("/login")
    public String login(
    		@Valid
    		@ModelAttribute("newLogin") LoginUser newLogin,
            BindingResult result,
            Model model,
            HttpSession session) {
    	
        User user = userService.login(newLogin, result);
        
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "index.jsp";
        }
        
        session.setAttribute("user_id", user.getId());
        return "redirect:/success";
    }
    
    @GetMapping("/success")
    public String success(
    		HttpSession session
    		) {
    	if (session.getAttribute("user_id") != null) {
    		return "success.jsp";    		
    	} else {
    		return "redirect:/";
    	}
    	
    }
    
    @GetMapping("/logout")
    public String logOut(HttpSession session) {
    	session.invalidate();
    	return "redirect:/";
    }
}
