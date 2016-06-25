package com.android.linglan.http.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/19 0019.
 */
public class UpdataPhotoBeen implements Serializable {
    public Data data = new Data();
    public class Data implements Serializable {
        public String mediaid;// 多媒体ID（必须）
        public String type;// 多媒体类型 0图片 1音频 2视频（必须）
        public String mediaurl;// url地址
        public String categoryid;// 分类ID
        public String categoryname;

        @Override
        public String toString() {
            return "Data{" +
                    "mediaid='" + mediaid + '\'' +
                    ", type='" + type + '\'' +
                    ", mediaurl='" + mediaurl + '\'' +
                    ", categoryid='" + categoryid + '\'' +
                    '}';
        }
    }
}
