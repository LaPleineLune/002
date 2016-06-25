package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class AllSearchListBean implements Serializable {
    public Data data;

    public class Data{
        public ArrayList<ArticleClassifyListBean> article;
//        public ArrayList<SubjectClassifyListBean> special;
    }
    public static class ArticleClassifyListBean implements Serializable {
        public String articleid;// 文章IDring catename;// 文章主分类
        //        public Strin
        public String title;// 文章标题
        public String photo;// 图片
        public String publishtime;// 文章发布时间
//        public Stg sub_catenames;// 文章副分类


    }

    public static class SubjectClassifyListBean implements Serializable {
        public String specialid;// 分类ID（必须）
        public String specialname;// 分类名称（必须）
        public String description;// 描述
        public String photo;// 图片
        public String orderid;// 分类排序
        public String addtime;// 发布时间

        @Override
        public String toString() {
            return "SubjectClassifyListBean{" +
                    "specialid='" + specialid + '\'' +
                    ", specialname='" + specialname + '\'' +
                    ", description='" + description + '\'' +
                    ", photo='" + photo + '\'' +
                    ", orderid='" + orderid + '\'' +
                    ", addtime='" + addtime + '\'' +
                    '}';
        }
    }

}
