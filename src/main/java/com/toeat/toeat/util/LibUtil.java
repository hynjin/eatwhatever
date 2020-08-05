package com.toeat.toeat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.toeat.toeat.vo.MetaVo;

public class LibUtil {
	private static final Logger logger = LoggerFactory.getLogger(LibUtil.class);

	private static Map<String, String> category;
	static{
		HashMap<String, String> type = new HashMap<String,String>();
		type.put("dessert","\"디저트\"");
		type.put("southern east","\"동남아음식\"");
		type.put("Indian","\"인도음식\"");
		type.put("western","\"양식\"");
		type.put("Korean","\"한식\"");
		type.put("Chinese","\"중식\"");
		type.put("Japanese","\"일식\"");
		type.put("snack","\"분식\"");
		type.put("fast food","\"패스트푸드\"");
		type.put("buffet","\"뷔페\"");
		type.put("alchol","\"술집\"");
		category = Collections.unmodifiableMap(type);
    }
    
	public static Collection<String> getFoodType(){
		return category.values();
	}
	
	public static List<String> getFoodType(String foodType){
		List<String> type = new ArrayList<String>();
		String[] elements = foodType.split(",");
		List<String> temp = new ArrayList<String>(Arrays.asList(elements));
		for(int i=0;i<temp.size();i++)
			type.add(category.get(temp.get(i)));
		return type;
	}
	public static MetaVo getMeta(String response) {
		JSONObject obj = new JSONObject(convStrToJSONObject(response));
		JSONObject meta = new JSONObject(convStrToJSONObject(obj.get("meta").toString()));
		MetaVo metaVo = new MetaVo(meta.get("total_count"), meta.get("is_end"), meta.get("pageable_count"), meta.get("same_name"));
		return metaVo;
	}
	
	public static JSONArray getDocuments(String response) {
		JSONObject obj = new JSONObject(convStrToJSONObject(response));
		JSONArray document = (JSONArray)obj.get("documents");
		return document;
	}

	public static JSONArray addDocuments(JSONArray arr, String str) {
		JSONObject obj = new JSONObject(convStrToJSONObject(str));
		JSONArray document = (JSONArray)obj.get("documents");
		arr.addAll(document);
		return arr;
	}
	/**
	 * Object 유효성 검증
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return StringUtils.isEmpty(value);
	}
	
	/**
	 * String 유효성 검증 
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		if (isEmpty(value) || value.toString().toUpperCase().equals("NULL") || value.toString().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * String을 JSONObject로 변경
     * @param str
     * @return
     */
    public static JSONObject convStrToJSONObject(String str) {
    	try {
	    	if (!isBlank(str)) {
	    		Object obj = JSONValue.parse(str);
	    		return (JSONObject) obj;
	    	} else {
	    		return (JSONObject) null;
	    	}
    	} catch (Exception ex) {
    		return (JSONObject) null;
    	}
    }
	
	 /**
     * Map를 JSONObject로 변경
     * @param map
     * @return JSONObject
     */
    public static JSONObject convMapToJSONObject(Map map) {
        String data = JSONObject.toJSONString(map);
        Object obj = JSONValue.parse(data);
        return (JSONObject) obj;
    }
    
    public static JSONArray shuffleJsonArray (JSONArray array) {
    	// Implementing Fisher–Yates shuffle
    	Random rnd = new Random();
    	rnd.setSeed(System.currentTimeMillis());
    	for (int i = array.size() - 1; i >= 0; i--)
    	{
    		int j = rnd.nextInt(i + 1);
    		// Simple swap
    		Object object = array.get(j);
    		array.set(j, array.get(i));
    		array.set(i, object);
           }
        return array;
    }
    
    public static JSONArray sortJsonArray(JSONArray array, String std) {
    	List<JSONObject> jsons = new ArrayList<JSONObject>();
    	for (int i = 0; i < array.size(); i++)
    	{
    		jsons.add((JSONObject)array.get(i));
    	}
    	Collections.sort(jsons, new Comparator<JSONObject>() {
    		@Override
    		public int compare(JSONObject lhs, JSONObject rhs) {
    			String lid = lhs.get(std).toString();
    			String rid = rhs.get(std).toString();
    			// Here you could parse string id to integer and then compare.
    			return lid.compareTo(rid);
    			}
    		});
    	JSONArray arr = new JSONArray();
    	for(int i=0;i<jsons.size();i++)
    		arr.add(jsons.get(i));
    	return arr;
    }
}