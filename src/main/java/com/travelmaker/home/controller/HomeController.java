package com.travelmaker.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	/*
	 * @RequestMapping(value = "/") public String home() { return "/main/main"; }
	 */
	
	@RequestMapping(value = "/")
	public String home() {
		return "main/main";
	}
	
}
