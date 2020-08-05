package com.toeat.toeat.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toeat.toeat.util.LibUtil;
import com.toeat.toeat.util.SearchUtil;


@Controller
@RequestMapping("/search")
public class SearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	@RequestMapping(value = "/test", produces="text/html; charset=UTF-8")
	public String test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(SearchUtil.getHeaders().toString());
		System.out.println(LibUtil.getFoodType());
		return "test/test";
	}
	
	@RequestMapping(value = "/home", produces="text/html; charset=UTF-8")
	public String home(HttpServletRequest request, HttpServletResponse response) throws IOException {

		
		return "test/test";
	}

	@RequestMapping(value = "/", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String search(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam Map<String,Object> param) {
        return SearchUtil.search(param).toString();
	}
}
