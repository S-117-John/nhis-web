package com.zebone.platform.framework.controller;

import com.zebone.platform.web.WebController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CommonController {
	
	static Logger logger = LogManager.getLogger(WebController.class.getName());
	
	@Autowired
	private WebController webController;
	
	@RequestMapping(value ={"/report/proxy/handle","/report/proxy/handle.zb"})
	public void handle(@RequestParam(required = false) String transCode,
                       @RequestParam(required = false) String param, HttpServletRequest request, HttpServletResponse response) {
		webController.handle(transCode, param, request, response);
	}
	

}
