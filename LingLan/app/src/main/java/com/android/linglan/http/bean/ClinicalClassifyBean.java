package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/28 0028.
 * 病历分类
 */
public class ClinicalClassifyBean implements Serializable {
    public List<ClinicalClassifyData> data;
    public static class ClinicalClassifyData implements Serializable {
        public String userid;// userid为0时是系统分类，其他时是用户自己的分类
        public String tagid;// 分类id
        public String tagname;// 分类名字
        public String cont;// 病例数量

    }
}
