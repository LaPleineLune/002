package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/20 0020.
 */
public class SubjectDetailsBean implements Serializable {
    public  ArrayList<SubjectList> data;
//    public class SubjectData implements Serializable {
//        public String favouriscancel;// 是否点赞 1否 0是
//        public int count_favour;// 点赞数
//        public String collectiscancel;// 是否收藏 1否 0是
//        public int count_collect;// 收藏数
//        public ArrayList<SubjectList> list;// 收藏数
        public class SubjectList implements Serializable {
            public String photo;// photo(文章)(专题大图)

            public String articleid;// 文章ID（必须）
            public String title;// 文章标题（必须）
//            public String author;// 文章作者
            public String authornames;// 文章作者
            public String publishtime;// 文章发布时间
            public String catename;// 文章主分类
            public String sub_catenames;// 文章副分类

//            "articleid": "506",
//                    "title": "谈谈个人治疗肝炎的体会",
//                    "photo": "http://img.zhongyishuyou.com/Public/data/article/logo/_20160309154445881256.jpg",
//                    "publishtime": "03月12日",
//                    "catename": "内科",
//                    "sub_catenames": ""

//            public String specialid;// 专题id
//            public String contentid;// 专题内容id
//            public String logo;// logo（小图）
//            public String photo;// photo(详情)
//            public String content_title;// 内容标题（图书为书名）
//            public String contenttype;// 内容类型 0 文章 1 医案 2 图书 3视频
//            public String author;// 作者名称
//            public String addtime;// 添加时间
//        }
    }
}
