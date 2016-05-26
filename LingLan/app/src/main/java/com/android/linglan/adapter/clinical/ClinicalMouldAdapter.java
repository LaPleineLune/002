package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalMouldBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/14 0014.
 * 病情模板
 */
public class ClinicalMouldAdapter extends RecyclerView.Adapter {
    private Activity context;
    public List<String> mChecked;
    private ArrayList<ClinicalMouldBean.ClinicalMouldData> clinicalMouldData;
//    private ArrayList<ClinicalMouldBean.ClinicalMouldData> mClinicalMouldData;
    private String flag;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public ClinicalMouldAdapter(Activity context) {
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_clinical_mould, parent, false);
        return new ClinicalMouldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalMouldViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalMouldData != null ? this.clinicalMouldData.size() : 0 ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ArrayList<ClinicalMouldBean.ClinicalMouldData> clinicalMouldData,ArrayList<ClinicalMouldBean.ClinicalMouldData> mClinicalMouldData){
        mChecked = new ArrayList<String>();

        if(clinicalMouldData != null && clinicalMouldData.size() != 0 ){
            mChecked.clear();
            getIsSelected().clear();
            for(int i = 0; i < clinicalMouldData.size(); i++) {
                mChecked.add("");
//                LogUtil.e("clinicalMouldData.get(i) " + clinicalMouldData.get(i).templetname);
                if(mClinicalMouldData !=null && mClinicalMouldData.size() != 0){
                    for(int j = 0; j < mClinicalMouldData.size();j++){
//                        LogUtil.e("mClinicalMouldData.get(i) " + mClinicalMouldData.get(j).templetname);
                        if(!clinicalMouldData.get(i).templetid.equals(mClinicalMouldData.get(j).templetid)){
                            getIsSelected().put(i, false);
                        }else {
                            getIsSelected().put(i, true);
//                            LogUtil.e("mClinicalMouldData.mClinicalMouldDatamClinicalMouldData(i) " + mClinicalMouldData.get(j).templetname);
                            flag = clinicalMouldData.get(i).templetname;
                            mChecked.set(i, flag);
                            break;
                        }
                    }
                }else{
                    getIsSelected().put(i, false);
                }
            }
        }

        this.clinicalMouldData = clinicalMouldData;
//        this.mClinicalMouldData = mClinicalMouldData;

        this.notifyDataSetChanged();

    }

    class ClinicalMouldViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_mould_name;
        private CheckBox checkbox_clinical_mould;
        public ClinicalMouldViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_mould_name = (TextView) rootView.findViewById(R.id.tv_mould_name);
            checkbox_clinical_mould = (CheckBox) rootView.findViewById(R.id.checkbox_clinical_mould);

        }

        public void bindData(final int position) {
            tv_mould_name.setText(context.getString(R.string.clinical_mould, clinicalMouldData.get(position).templetname));

            if(getIsSelected() != null && getIsSelected().size() != 0){
                checkbox_clinical_mould.setChecked(getIsSelected().get(position));
//                LogUtil.e("getIsSelected().get(position) ==" + getIsSelected().get(position) + "   position" + position);
            }

            rootView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (isSelected.get(position)) {
                        isSelected.put(position, false);
                        setIsSelected(isSelected);

                        flag = "";
                        mChecked.set(position, "");
//                        LogUtil.e("去掉了了了" + mChecked.get(position));
                    } else {
                        isSelected.put(position, true);
                        setIsSelected(isSelected);

                        flag = clinicalMouldData.get(position).templetname;
                        mChecked.set(position, flag);
//                        LogUtil.e(mChecked.get(position));
                    }

                    checkbox_clinical_mould.setChecked(getIsSelected().get(position));

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ClinicalMouldAdapter.isSelected = isSelected;
    }
}
