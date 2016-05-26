package com.android.linglan.http.bean;

public class Body {
	public BodyData data;
	public class BodyData {
		public String number;// 最新版本号
		public int isupdate;// 是否有更新 1是 0否
		public String description;// app描述
		public int isforce;// 是否强制更新 1是 0否
	}

}
