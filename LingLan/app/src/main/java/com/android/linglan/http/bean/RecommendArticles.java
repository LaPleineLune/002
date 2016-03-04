package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class RecommendArticles implements Serializable{
public ArrayList<RecommendArticle> data;

    public static class RecommendArticle implements Serializable {
        public String articleid;//文章Id
        public String title;//文章标题
        public String photo;//配图
        public String addtime;//添加时间

        @Override
        public String toString() {
            return "RecommendArticle{" +
                    "articleid='" + articleid + '\'' +
                    ", title='" + title + '\'' +
                    ", photo='" + photo + '\'' +
                    ", addtime='" + addtime + '\'' +
                    '}';
        }
    }

}
