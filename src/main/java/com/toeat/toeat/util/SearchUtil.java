package com.toeat.toeat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import com.toeat.toeat.vo.MetaVo;
import com.toeat.toeat.vo.ResponseVo;


@Component
public class SearchUtil{
	private static final Logger logger = LoggerFactory.getLogger(SearchUtil.class);

	private static String restAPIKey;
    private static final String API_HOST  = "https://dapi.kakao.com";
    private static final String SEARCH_PATH = "/v2/local/search/keyword.json";

	
	@Value("${kakao.restapi.key}")
    public void setRestAPIKey(String key) {
        restAPIKey = key;
    }
	
	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + restAPIKey);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		return headers;
	}
	
	public static String getQuery(Map<String,Object> param, int page, String foodType) throws IOException {
		String query = "";
		String str = foodType;

		if(param.containsKey("query"))
			str += "," + param.get("query").toString();

		if(param.containsKey("currPosX") && param.containsKey("currPosY"))
		{
			double x = Double.parseDouble(param.get("currPosX").toString());
			double y = Double.parseDouble(param.get("currPosY").toString());
		
			if(param.containsKey("radius"))
			{
				int radius = Integer.parseInt(param.get("radius").toString());
				query = getQuery(str, page, x, y, radius );
			}
			else
				query = getQuery(str, page, x, y);
		}
		else if(param.containsKey("rect"))
		{
			String rect = param.get("rect").toString();
			query = getQuery(str, page, rect );
		}
		else
			query = getQuery(str, page);
		logger.debug(query);
		return query;
	}

	public static String getQuery(String keywords, int page) throws IOException {
		String query = "?query=";
		query += URLEncoder.encode(keywords,"UTF-8");
		query += "&page=" + page;
		return query;
	}
	
	public static String getQuery(String keywords, int page, double x, double y) throws IOException {
		String query = "?query=";
		query += URLEncoder.encode(keywords,"UTF-8");
		query += "&page=" + page;
		query += "&x=" + x;
		query += "&y=" + y;
		return query;
	}
	
	public static String getQuery(String keywords, int page, double x, double y, int radius) throws IOException {
		String query = "?query=";
		query += URLEncoder.encode(keywords,"UTF-8");
		query += "&page=" + page;
		query += "&x=" + x;
		query += "&y=" + y;
		query += "&radius=" + radius;
		return query;
	}
	
	public static String getQuery(String keywords, int page, String rect) throws IOException {
		String query = "?query=";
		query += URLEncoder.encode(keywords,"UTF-8");
		query += "&page=" + page;
		query += "&rect=" + rect;
		return query;
	}
	
	public static URI getURL(Map<String,Object> param, int page, String foodType) throws IOException {
		URI url = URI.create(API_HOST+SEARCH_PATH+getQuery(param,page,foodType));
		return url;
	}
	
	public static URI getURL(String keywords, int page) throws IOException {
		URI url = URI.create(API_HOST+SEARCH_PATH+getQuery(keywords,page));
		return url;
	}

	public static URI getURL(String keywords, int page, double x, double y) throws IOException {
		URI url = URI.create(API_HOST+SEARCH_PATH+getQuery(keywords,page,x,y));
		return url;
	}

	public static URI getURL(String keywords, int page, double x, double y, int radius) throws IOException {
		URI url = URI.create(API_HOST+SEARCH_PATH+getQuery(keywords,page,x,y,radius));
		return url;
	}
	
	public static URI getURL(String keywords, int page, String rect) throws IOException {
		URI url = URI.create(API_HOST+SEARCH_PATH+getQuery(keywords,page,rect));
		return url;
	}
	
	public static ResponseEntity<String> getSearch(String keywords) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> request = new RequestEntity<>(getHeaders(), HttpMethod.GET, getURL(keywords,1));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        return response;
    }
	
	public static ResponseVo getSearchVo(String keywords, int page) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> request = new RequestEntity<>(getHeaders(), HttpMethod.GET, getURL(keywords,page));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        HttpStatus status = response.getStatusCode();
        if (status == HttpStatus.OK) {
			return new ResponseVo("OK", response.getBody().toString());
		} else {
			return new ResponseVo(status.toString(), response.getBody().toString());
		}
    }
	
	public static ResponseVo getSearchVo(Map<String,Object> param, int page, String foodType) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> request = new RequestEntity<>(getHeaders(), HttpMethod.GET, getURL(param,page,foodType));
        logger.debug( getURL(param,page,foodType).toString());
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        HttpStatus status = response.getStatusCode();
        if (status == HttpStatus.OK) {
			return new ResponseVo("OK", response.getBody().toString());
		} else {
			return new ResponseVo(status.toString(), response.getBody().toString());
		}
    }
	
	public static String search(Map<String,Object> param) {
		JSONObject obj = new JSONObject();
		List<String> foodType;
		
		if(param.containsKey("foodType"))
			foodType = LibUtil.getFoodType(param.get("foodType").toString());
		else
			foodType = new ArrayList<String>(LibUtil.getFoodType());
		
        try {
        	for(int i=0;i < foodType.size();i++)
        	{
        		int page = 1;
        		JSONArray arr = new JSONArray();
        		ResponseVo responseVo = new ResponseVo();
        		MetaVo metaVo = new MetaVo();
        		String status = "OK";
        		metaVo.setIsEnd("false");
	        	
        		while(status == "OK" && metaVo.getIsEnd() == "false")
	        	{
	        		responseVo = getSearchVo(param,page,foodType.get(i));
	        		metaVo = LibUtil.getMeta(responseVo.getBody());
	        		status = responseVo.getCode();
	        		LibUtil.addDocuments(arr,responseVo.getBody());
	        		page++;
	        	}
	        	arr = LibUtil.shuffleJsonArray(arr);
	        	obj.put(foodType.get(i), arr.toString());
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	public static String search(String keywords) {
		int page = 1;
		String status = "OK";
		JSONArray obj = new JSONArray();
		ResponseVo responseVo = new ResponseVo();
		MetaVo metaVo = new MetaVo();
		metaVo.setIsEnd("false");
        try {
        	while(status == "OK" && metaVo.getIsEnd() == "false")
        	{
        		responseVo = getSearchVo(keywords,page);
        		metaVo = LibUtil.getMeta(responseVo.getBody());
        		status = responseVo.getCode();
        		LibUtil.addDocuments(obj,responseVo.getBody());
        		page++;
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        obj = LibUtil.shuffleJsonArray(obj);
		return obj.toString();
	}
	
}