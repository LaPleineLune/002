package com.android.linglan.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/4/19 0019.
 * json的生成与解析工具
 */
public class GsonTools {

	public GsonTools() {
		// TODO Auto-generated constructor stub
	}

	public static String createGsonString(Object object) {
		Gson gson = new Gson();
	
		String gsonString = gson.toJson(object);
		return gsonString;
	}

	public static <T> T fromGsonToBean(String gsonString, Class<T> cls) {
		Gson gson = new Gson();
		T t = gson.fromJson(gsonString, cls);
		return t;
	}

	public static <T> List<T> fromGsonToList(String gsonString, Class<T> cls) {
		Gson gson = new Gson();
		List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
		return list;
	}

	public static <T> List<Map<String, T>> fromGsonToListMaps(
			String gsonString) {
		List<Map<String, T>> list = null;
		Gson gson = new Gson();
		list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
		}.getType());
		return list;
	}

	public static <T> Map<String, T> fromGsonToMaps(String gsonString) {
		Map<String, T> map = null;
		Gson gson = new Gson();
		map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {

		}.getType());
		return map;
	}

//	public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "jack");
//		map.put("age", 23);
//        String gsonString = GsonTools.createGsonString(map);
//        LogUtil.e("我要看的数据=" + gsonString);// {"age":23,"name":"jack"}

//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "jack");
//		map.put("age", 23);
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("name", "rose");
//		map2.put("age", 24);
//		list.add(map);
//		list.add(map2);
//		String gsonString = GsonTools.createGsonString(list);
//        LogUtil.e("我要看的数据11=" + gsonString);// 我要看的数据11=[{"age":23,"name":"jack"},{"age":24,"name":"rose"}]
//		List<Map<String, Object>> list2 = GsonTools.fromGsonToListMaps(gsonString);
//        LogUtil.e("我要看的数据22=" + list2.toString());// 我要看的数据22=[{age=23.0, name=jack}, {age=24.0, name=rose}]
//	}

}
