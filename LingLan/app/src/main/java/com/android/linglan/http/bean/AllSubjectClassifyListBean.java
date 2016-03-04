package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/22 0022.
 * 专题全部分类
 */
public class AllSubjectClassifyListBean implements Serializable {
    public ArrayList<SubjectClassifyListBean> data;
    public static class SubjectClassifyListBean implements Serializable {
        public String cateid;// 分类ID（必须）
        public String catename;// 分类名称（必须）
        public String orderid;// 分类排序
        public String addtime;// 发布时间

        public SubjectClassifyListBean(String cateid, String catename, String orderid, String addtime) {
            this.cateid = cateid;
            this.catename = catename;
            this.orderid = orderid;
            this.addtime = addtime;
        }

        @Override
        public String toString() {
            return "SubjectClassifyListBean{" +
                    "cateid='" + cateid + '\'' +
                    ", catename='" + catename + '\'' +
                    ", orderid='" + orderid + '\'' +
                    ", addtime='" + addtime + '\'' +
                    '}';
        }
    }
}
