package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29 0029.
 */
public class SearchArticleBean {

    public ArrayList<ArticleClassifyListBean> list;

    public static class ArticleClassifyListBean implements Serializable {
        public String articleid;// 文章ID
        public String title;// 文章标题
        public String photo;// 图片
        public String publishtime;// 文章发布时间
        public String authornames;// 作者名
        public String istop;
        public String isrecommend;//是否推荐
        public String catename;// 文章主分类
        public String sub_catenames;// 文章副分类

    }
}
