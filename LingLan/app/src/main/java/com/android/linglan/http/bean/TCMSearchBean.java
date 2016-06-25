package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by LeeMy on 2016/5/4 0004.
 * 中医搜搜-搜索关键字
 */
public class TCMSearchBean implements Serializable {
    public TCMSearchData data;
    public class TCMSearchData implements Serializable {
        public String id;// id
        public String time;// 搜索用时
        public String total;// 显示条数
        public String title;// 标题
        public String content;// 内容
        public String source;// 来源

    }
}
