package com.android.linglan.http.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class CityBeen {
    public ArrayList<Province> subdata;
    public class Province{
        public String id;
        public String name;
        public String ename;
        public String serial;
        public  String sort;
        public  String parentid;
        public String namecode;
//        public ArrayList<City> subdata;

        @Override
        public String toString() {
            return "Province{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", ename='" + ename + '\'' +
                    ", serial='" + serial + '\'' +
                    ", sort='" + sort + '\'' +
                    ", parentid='" + parentid + '\'' +
                    ", namecode='" + namecode + '\'' +
//                    ", subdata=" + subdata +
                    '}';
        }
//        public class City {
//            public String id;
//            public String name;
//            public String ename;
//            public String serial;
//            public String sort;
//            public String parentid;
//            public String namecode;
//
//            @Override
//            public String toString() {
//                return "City{" +
//                        "id='" + id + '\'' +
//                        ", name='" + name + '\'' +
//                        ", ename='" + ename + '\'' +
//                        ", serial='" + serial + '\'' +
//                        ", sort='" + sort + '\'' +
//                        ", parentid='" + parentid + '\'' +
//                        ", namecode='" + namecode + '\'' +
//                        '}';
//            }
//        }
    }

}
