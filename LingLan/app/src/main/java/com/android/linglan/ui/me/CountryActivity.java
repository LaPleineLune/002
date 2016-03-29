package com.android.linglan.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.bean.CityModel;
import com.android.linglan.http.bean.CountryModel;
import com.android.linglan.http.bean.ProvinceModel;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends BaseActivity implements  AdapterView.OnItemClickListener {
    private ListView lv_area;
    private ProvinceAdapter pAdapter;

    private List<ProvinceModel> provinceList;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_area);
    }

    @Override
    protected void initView() {
        lv_area = (ListView) findViewById(R.id.lv_area);
    }

    @Override
    protected void initData() {
        setTitle("所在地区", "");

        try {
            parseAddress(getFromRaw());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        pAdapter = new ProvinceAdapter(provinceList);
        lv_area.setAdapter(pAdapter);


    }

    @Override
    protected void setListener() {
        // 判断该省下是否有市级
        lv_area.setOnItemClickListener(this);
    }

    private void parseAddress(String data) throws XmlPullParserException {
        try {
            ProvinceModel provinceModel = null;
            CityModel cityModel = null;
            CountryModel countyModel = null;
            List<CityModel> cityList = null;
            List<CountryModel> countyList = null;

            InputStream xmlData = new ByteArrayInputStream(
                    data.getBytes("UTF-8"));
            XmlPullParserFactory factory = null;
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser;
            parser = factory.newPullParser();
            parser.setInput(xmlData, "utf-8");
            String province;
            String city;
            String county;
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                String typeName = parser.getName();
                if (type == XmlPullParser.START_TAG) {
                    if ("root".equals(typeName)) {
                        provinceList = new ArrayList<ProvinceModel>();
                    } else if ("province".equals(typeName)) {
                        province = parser.getAttributeValue(0);// 获取标签里第一个属性,例如<city
                        // name="北京市"
                        // index="1">中的name属性
                        provinceModel = new ProvinceModel();
                        provinceModel.setProvince(province);
                        cityList = new ArrayList<CityModel>();
                    } else if ("city".equals(typeName)) {
                        city = parser.getAttributeValue(0);
                        cityModel = new CityModel();
                        cityModel.setCity(city);
                        countyList = new ArrayList<CountryModel>();
                    } else if ("area".equals(typeName)) {
                        county = parser.getAttributeValue(0);
                        countyModel = new CountryModel();
                        countyModel.setCounty(county);
                    }
                } else if (type == XmlPullParser.END_TAG) {
                    if ("root".equals(typeName)) {
                    } else if ("province".equals(typeName)) {
                        provinceModel.setCity_list(cityList);
                        provinceList.add(provinceModel);
                    } else if ("city".equals(typeName)) {
                        cityModel.setCounty_list(countyList);
                        cityList.add(cityModel);
                    } else if ("area".equals(typeName)) {
                        countyList.add(countyModel);
                    }
                } else if (type == XmlPullParser.TEXT) {
                    typeName = null;
                }
                type = parser.next();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFromRaw() {
        InputStream open = getResources().openRawResource(R.raw.address);
        InputStreamReader isr = new InputStreamReader(open);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (provinceList.get(position).getCity_list().size() < 1) {
            ToastUtil.show("没有下级了");
        }else{
            Intent intent = new Intent(CountryActivity.this, ProvinceActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    class ProvinceAdapter extends BaseAdapter {
        public List<ProvinceModel> adapter_list;
        private LayoutInflater layoutInflater;

        public ProvinceAdapter(List<ProvinceModel> list) {
            adapter_list = list;
            layoutInflater = LayoutInflater.from(CountryActivity.this);
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public ProvinceModel getItem(int arg0) {
            return adapter_list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.layout_area, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            setView(viewHolder, position);
            return convertView;
        }

        public void setView(ViewHolder viewHolder, int position) {
            viewHolder.tv_area.setText(adapter_list.get(position).getProvince());
        }

        class ViewHolder {
            public TextView tv_area;
        }

//        @Override
//        public View getView(int position, View arg1, ViewGroup arg2) {
//            TextView tv = new TextView(CountryActivity.this);
//            tv.setPadding(dpToPx(5), dpToPx(10), 0, dpToPx(10));
//            tv.setTextSize(18);
//            tv.setText(adapter_list.get(position).getProvince());
//            return tv;
//        }

    }

    class CountryAdapter extends BaseAdapter {
        public List<CountryModel> adapter_list;

        public CountryAdapter(List<CountryModel> list) {
            adapter_list = list;
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public CountryModel getItem(int arg0) {
            return adapter_list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {
            TextView tv = new TextView(CountryActivity.this);
            tv.setPadding(dpToPx(5), dpToPx(10), 0, dpToPx(10));
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getCounty());
            return tv;
        }

    }

    private int dpToPx(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
