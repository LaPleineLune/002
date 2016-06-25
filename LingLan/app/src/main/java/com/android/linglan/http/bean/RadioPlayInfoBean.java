package com.android.linglan.http.bean;

import java.io.Serializable;

/**
 * Created by LeeMy on 2016/6/21 0021.
 * 播放详情的头部（即无列表的展示内容）
 */
public class RadioPlayInfoBean implements Serializable {
    public RadioPlayInfoData data;
    public class RadioPlayInfoData implements Serializable {
        public String audioid;// 音频ID
        public String title;// 分类名称
        public String logo;// 分享图片
        public String photo;// 图片
        public String description;// 简介
        public String content;// 内容
        public String url;// 音频地址
        public String albumid;// 专辑ID
        public String url_createtime;//
        public String boskey;//
        public String bucket;//
        public String iscollect;// 是否收藏 0未收藏    1已收藏
    }
}