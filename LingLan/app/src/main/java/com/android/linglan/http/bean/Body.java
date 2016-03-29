package com.android.linglan.http.bean;

public class Body {
	public BodyData data;
//	public String isExist = "";
//	public String message;
//	public String[] content;
//	public String title;
//	public String version;
//	public String url;
//  public int forceUpdate;
//  // MD5 verification
//  public String mdv;
//  public String[] marketWhiteList;

	public class BodyData {
		public String number;// 最新版本号
		public int isupdate;// 是否有更新 1是 0否
		public String description;// app描述
		public int isforce;// 是否强制更新 1是 0否
	}

}
