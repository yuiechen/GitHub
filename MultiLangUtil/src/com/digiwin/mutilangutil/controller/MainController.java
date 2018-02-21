package com.digiwin.mutilangutil.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.digiwin.v6.V6Const;
@Controller
public class MainController {
	@RequestMapping(value="/entry", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userName");
		String v6Token = request.getParameter("password");
		request.getSession().setAttribute(V6Const.V6_BO_USER_ID, userId);
		request.getSession().setAttribute(V6Const.BO_ENTERPRISESESSION_BY_TOKEN, v6Token);
		return new ModelAndView("WechatFrame");
	}
}
