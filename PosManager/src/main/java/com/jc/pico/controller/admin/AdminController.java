/*
 * Filename	: AdminController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	protected static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("dataMain", "dashboard");
		return "admin/index";
	}

	@RequestMapping("/{page}")
	public String router(Model model, @PathVariable String page){
		model.addAttribute("dataMain", page.toLowerCase());
		return "admin/"+page.toLowerCase();
	}
}
