package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/20 0020.
 */
public class AuthorDetailsBean implements Serializable {
    public ArrayList<AuthorDetailsData> data;
    public class AuthorDetailsData implements Serializable {
        public String name;// 姓名
        public String about;// 简介
        public String count_followed;// 关注人
        public String count_article;// 文章数据量
        public String isapprove;// 是否为认证作者：1为认证0为非认证
    }
}
