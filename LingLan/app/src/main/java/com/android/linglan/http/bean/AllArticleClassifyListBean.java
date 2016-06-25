package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class AllArticleClassifyListBean implements Serializable {
    public ArrayList<ArticleClassifyListBean> data;
    public static class ArticleClassifyListBean implements Serializable {
        public String articleid;// 文章ID
        public String title;// 文章标题
        public String photo;// 图片
        public String logo;// 图片
        public String publishtime;// 文章发布时间
        public String catename;// 文章主分类
        public String sub_catenames;// 文章副分类
        public String authornames;//作者
        public String isnew;// 是否是今日更新

    }
}
