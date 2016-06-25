package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/20 0020.
 * 全部音频分类列表
 */
public class RadioClassifyBean implements Serializable {
    public ArrayList<RadioClassifyData> data;
    public static class RadioClassifyData implements Serializable {
        public String cateid;// 分类ID（必须）
        public String catename;// 分类ID（必须）
    }
}
