package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 病历模板
 */
public class ClinicalMouldBean implements Serializable {
    public ArrayList<ClinicalMouldData> data;
    public static class ClinicalMouldData implements Serializable {
        public String templetname;

        public String getTempletid() {
            return templetid;
        }

        public void setTempletid(String templetid) {
            this.templetid = templetid;
        }

        public String templetid;

        public String getTempletname() {
            return templetname;
        }

        public void setTempletname(String templetname) {
            this.templetname = templetname;
        }
    }
}
