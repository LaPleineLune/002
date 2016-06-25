package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/21 0021.
 * 专辑详情列表
 */
public class RadioSpecialListBean implements Serializable {
    public ArrayList<RadioSpecialListData> data;
    public class RadioSpecialListData implements Serializable {
        public String audioid;// 音频ID
        public String title;// 音频名称
        public String count_view;// 播放次数
        public String duration;// 播放时长
        public String publishtime;// 发布时间
        public String url;// 播放地址
    }
}