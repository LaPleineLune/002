package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalManageClassifyActivity;

import java.util.List;

/**
 * Created by Leemy on 2016/4/27 0027.
 * 新建分类列表的Adapter
 */
public class ClinicalCreateClassifyAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    public String editContent = "";

    public ClinicalCreateClassifyAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_classify, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalClassifyData != null ? this.clinicalClassifyData.size() + 1 : 0 ;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData){
        this.clinicalClassifyData = clinicalClassifyData;
        this.notifyDataSetChanged();
    }

    public String getEditContent() {
        return editContent;
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_classify;
        private EditText edt_create_classify;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_classify = (TextView) rootView.findViewById(R.id.tv_classify);
            edt_create_classify = (EditText) rootView.findViewById(R.id.edt_create_classify);

//            // 获取编辑框焦点
//            edt_create_classify.setFocusable(true);
//            //打开软键盘
//            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        public void bindData(final int position) {
            if (position == clinicalClassifyData.size()) {
                tv_classify.setVisibility(View.GONE);
                edt_create_classify.setVisibility(View.VISIBLE);
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                edt_create_classify.requestFocus();
                //打开软键盘
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if (position != clinicalClassifyData.size() && position < clinicalClassifyData.size()) {
                tv_classify.setFocusable(false);
                tv_classify.setText(clinicalClassifyData.get(position).tagname);
            }
            edt_create_classify.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editContent = edt_create_classify.getText().toString();
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}