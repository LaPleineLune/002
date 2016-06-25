package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/22 0022.
 * 病历详情列表
 */
public class ClinicalDetailsBean implements Serializable {
    public ArrayList<ClinicalDetailsData> data;
    public static class ClinicalDetailsData implements Serializable {
        public String courseid;// 病程id
        public String visittime;// 病程时间
        public String feature;// 病情记录（病症）
        public ArrayList<ClinicalDetailsImg> img;// 图片
        public ArrayList<ClinicalDetailsPathography> pathography;// 病情记录
        public static class ClinicalDetailsImg implements Serializable {
            public String mediaid;// 多媒体ID（必须）
            public String mediaurl;// 图片URL
            public String categoryname;// 类型（处方、舌象等）
            public String categoryid;// 类型id（处方、舌象等）
        }
        public static class ClinicalDetailsPathography implements Serializable {
            public String templetid;// 模板id
            public String templetname;// 模板名称
            public String content;// 病情记录的内容
        }
    }
}
