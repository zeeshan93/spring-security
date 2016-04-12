package com.compassites.testproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/operation")
public class DataController {
	@RequestMapping(value = {"/message"}, method = RequestMethod.GET)
	@ResponseBody
	public String messageMethod(@RequestHeader(value = "auth_key") String authKey){
		return "Message acquired";
	}
}
