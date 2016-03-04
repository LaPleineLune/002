package com.android.linglan.http.bean;

import java.util.Arrays;

public class Body {
	public String code;
	public BodyData data;
	public String isExist = "";
	public String message;
	public String[] content;
	public String title;
	public String version;
	public String url;
  public int forceUpdate;
  // MD5 verification
  public String mdv;
  public String[] marketWhiteList;

	public class BodyData {
		public String update;// yes/no有更新为yes 不更新为no
	}

	@Override
	public String toString() {
		return "Body [code=" + code + ", isExist=" + isExist + ", message="
				+ message + ", content=" + Arrays.toString(content)
				+ ", title=" + title + ", version=" + version + ", url=" + url
				+ "]";
	}

}
