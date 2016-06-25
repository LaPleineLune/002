package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29 0029.
 */
public class SearchFMBean {

    public ArrayList<FmClassifyListBean> list;

    public static class FmClassifyListBean implements Serializable {
        public String type;// 单个音频 5，专辑6
        public String audioid;// 音频id
        public String title;// 标题
        public String count_view;// 播放次数
        public String duration;//时长
        public String publishtime;// 文章发布时间
        public String logo;//图标
        public String albumid;// 专辑id
        public String albumname;// 专辑名称
        public String count_play;// 播放次数
        public String count_chapter;// 专辑里面的音频个数
        public String description;// 专辑描述

    }
}
