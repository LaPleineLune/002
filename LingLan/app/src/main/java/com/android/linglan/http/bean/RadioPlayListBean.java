package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/21 0021.
 * 播放详情列表（即音频所属的列表）
 */
public class RadioPlayListBean implements Serializable {
    public ArrayList<RadioPlayListData> data;
    public class RadioPlayListData implements Serializable {
        public String audioid;// 音频ID
        public String title;// 音频名称
        public String url;// url
//        public String count_view;// 播放次数
//        public String duration;// 播放时长
//        public String publishtime;// 发布时间
    }
}