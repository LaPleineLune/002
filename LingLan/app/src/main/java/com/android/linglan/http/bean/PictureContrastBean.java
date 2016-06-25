package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/30 0030.
 * 图片对比的Bean
 */
public class PictureContrastBean implements Serializable {
    public ArrayList<PictureContrastData> data;
    public class PictureContrastData implements Serializable {
        public String visittime;// 时间：2016.08.08
        public ArrayList<PictureContrastMedia> media;
        public class PictureContrastMedia implements Serializable {
            public String mediaid;// 多媒体id
            public String mediaurl;// 多媒体URL
        }
    }
}
