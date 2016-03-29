package com.android.linglan.http.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class CountryBeen {
    public ArrayList<Country> data;
    public class Country{
        public String id;
        public String name;
        public String ename;
        public String serial;
        public String sort;
        public String parentid;
        public String namecode;
        public ArrayList<CityBeen> subdata;

        @Override
        public String toString() {
            return "Country{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", ename='" + ename + '\'' +
                    ", serial='" + serial + '\'' +
                    ", sort='" + sort + '\'' +
                    ", parentid='" + parentid + '\'' +
                    ", namecode='" + namecode + '\'' +
                    '}';
        }
    }
}
