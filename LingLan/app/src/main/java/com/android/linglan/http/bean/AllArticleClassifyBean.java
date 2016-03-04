package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/21 0021.
 */
public class AllArticleClassifyBean implements Serializable {
    public ArrayList<ArticleClassifyBean> data;
    public static class ArticleClassifyBean implements Serializable {
        public String cateid;// 分类ID（必须）
        public String catename;// 分类名称（必须）
        public String orderid;// 分类排序

//        public ArticleClassifyBean(String cateid, String catename, String orderid) {
//            this.cateid = cateid;
//            this.catename = catename;
//            this.orderid = orderid;
//        }

//        public String parentid;// 父分类ID（必须）
//        public String isleaf;// 是否是叶子分类
//        public String status;// 状态


        @Override
        public String toString() {
            return "ArticleClassifyBean{" +
                    "cateid='" + cateid + '\'' +
                    ", catename='" + catename + '\'' +
                    ", orderid='" + orderid + '\'' +
                    '}';
        }
    }

}
