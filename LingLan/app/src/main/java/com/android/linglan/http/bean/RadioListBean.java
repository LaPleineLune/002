package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/20 0020.
 * 全部音频分类列表
 */
public class RadioListBean implements Serializable {
    public ArrayList<RadioListData> data;
    public static class RadioListData implements Serializable {
        public String type;// 类型    5单音频    6音频专辑

        public String albumid;// 专辑ID(单音频所属专辑id)
        public String description;// 简介/描述


        public String albumname;// 专辑名称
        public String logo;// 小图片
        public String count_chapter;// 集数
        public String count_play;// 播放次数


        public String audioid;// 音频ID
        public String title;// 分类名称
        public String photo;// 图片
        public String count_view;//
        public String duration;//
        public String publishtime;//
//        public String description;// 描述
        public String content;//
        public String url;// 播放地址
        public String author;// 主播
//        public String albumid;// 单音频所属专辑id
    }
}
