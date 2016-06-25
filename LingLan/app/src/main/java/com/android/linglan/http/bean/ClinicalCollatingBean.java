package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/20 0020.
 */
public class ClinicalCollatingBean implements Serializable {
    public ClinicalCollatingData data;
    public static class ClinicalCollatingData implements Serializable {
        public String casecon;// (用户自己创建的)病例数量
        public String usercasecon;// (包含未命名病历的)病例数量 大于0，说明已创建属于自己的病历，小于等于0，说明没有创建属于自己的病历
        public String democasecon;//事例病例个数
        public String tagnames;//当前fenl
        public ArrayList<ClinicalCollatingList> list;// 病历ID
        public static class ClinicalCollatingList implements Serializable {
            public String illnesscaseid;// 病历ID
            public String patientname;// 患者名称
            public String patientid;// 患者id
            public String feature;// 病证
            public String lastvisittime;// 最后就诊时间
            public int isdemo;// 是否是示例病历    1是      0不是
            public int page;
        }
}

//            "data": {
//        "list": [
//        {
//            "illnesscaseid": "1",
//                "patientid": "1",
//                "patientname": "（示例病历）",
//                "feature": "糖尿病",
//                "lastvisittime": "2016.05.11 ",
//                "isdemo": "1"
//        }
//        ],
//        "casecon": 1,
//                "usercasecon": 0
//    }

}
