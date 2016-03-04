package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/3/1 0001.
 */
public class HomepageRecommendBean implements Serializable {
    public ArrayList<HomepageRecommendBeanData> data;
    public class HomepageRecommendBeanData implements Serializable {
        public String type;// 0文章   4专题
        public String photo;// logo

        public String articleid;// 文章ID（必须）
        public String title;// 文章标题（必须）
        public String author;// 文章作者
        public String publishtime;// 文章发布时间
        public String catename;// 文章主分类
        public String sub_catenames;// 文章副分类

        public String specialid;// 专题ID（必须）
        public String specialname;// 专题名称（必须）
        public String description;// 描述
        public String addtime;// 创建时间
        public String orderid;// 排序
        public String content_title;// 最新添加的文章标题

    }
}
