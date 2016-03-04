package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29 0029.
 */
public class SearchSubjectBean {

    public ArrayList<SubjectClassifyListBean> special;

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
