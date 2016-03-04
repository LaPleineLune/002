package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SortModel implements Serializable {
	public ArrayList<SortModelData> data;

	public class SortModelData implements Serializable {
		public String citycode;// 城市code
		public String cityname;// 城市名称
	}

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
