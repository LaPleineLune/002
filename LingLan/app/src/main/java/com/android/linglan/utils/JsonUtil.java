package com.android.linglan.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class JsonUtil {
	public static <T> T json2Bean(String result, Class<T> clz) {
		Gson gson = new Gson();
    try {
      T t = gson.fromJson(result, clz);
      return t;
    } catch (JsonSyntaxException exception) {
      exception.printStackTrace();
      Log.w("GSON Util", exception.toString());
    }
		return null;
	}
}
