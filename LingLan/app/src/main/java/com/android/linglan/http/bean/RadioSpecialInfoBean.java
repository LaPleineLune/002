package com.android.linglan.http.bean;

import java.io.Serializable;

/**
 * Created by LeeMy on 2016/6/21 0021.
 * 专辑详情的头部
 */
public class RadioSpecialInfoBean implements Serializable {
    public RadioSpecialInfoData data;
    public class RadioSpecialInfoData implements Serializable {
        public String albumid;// 专辑ID
        public String albumname;// 专辑名称
        public String anchor;// 主播
        public String author;// 文字作者
        public String photo;// 图片
        public String description;// 简介
        public String count_chapter;// 集数
        public String count_play;// 播放次数
        public String iscollect;// 是否收藏 0未收藏    1已收藏
    }
}