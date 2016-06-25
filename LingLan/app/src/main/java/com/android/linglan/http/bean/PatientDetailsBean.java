package com.android.linglan.http.bean;

import java.io.Serializable;

/**
 * Created by LeeMy on 2016/4/22 0022.
 * 患者信息详情
 */
public class PatientDetailsBean implements Serializable {
    public PatientDetailsData data;
    public static class PatientDetailsData implements Serializable {
        public String patientid;// 患者ID
        public String patientname;// 患者名称
        public String sex;// 患者性别
        public String ageyear;// 患者年龄
        public String agemonth;// 患者的“个月”
        public String phone;// 患者的手机
    }
}
