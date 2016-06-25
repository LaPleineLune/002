package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/20 0020.
 */
public class SubjectInfoBean implements Serializable {
    public Data data = new Data();

    public class Data implements Serializable {

        public ArrayList<typeClassisy> navi;
        public int favouriscancel;// 是否点赞 1否 0是
        public int count_favour;// 点赞数count_favour
        public int collectiscancel;// 是否收藏 1否 0是
        public int count_collect;// 收藏数
        public String specialid;// 专题id

        public class typeClassisy implements Serializable {
            public int type;// 类型 ,0文章 5电台
            public String text;// 标题
        }

    }
//
//                    "data": {
//                "collectiscancel": 1,
//                        "favouriscancel": 1,
//                        "count_favour": 4,
//                        "count_collect": 18,
//                        "specialid": "51",
//                        "navi": [
//                {
//                    "type": 0,
//                        "text": "文章"
//                }
//                ]

}
