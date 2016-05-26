package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SortModel implements Serializable {
//	public ArrayList<SortModelData> data;
//
//	public class SortModelData implements Serializable {
//		public String citycode;// 城市code
//		public String cityname;// 城市名称
//	}

	private String name;   //显示的数据
	private String id;   //显示的数据的id
	private String feature;   // 病证
	private String lastvisittime;   // 最后就诊时间
	private String sortLetters;  //显示数据拼音的首字母
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getLastvisittime() {
		return lastvisittime;
	}

	public void setLastvisittime(String lastvisittime) {
		this.lastvisittime = lastvisittime;
	}

	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
