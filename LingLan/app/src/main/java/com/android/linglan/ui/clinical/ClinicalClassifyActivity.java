package com.android.linglan.ui.clinical;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.flowlayout.TagAdapter;
import com.android.linglan.widget.flowlayout.TagFlowLayout;

/**
 * Created by LeeMy on 2016/4/26 0026.
 * 分类（标签）
 */
public class ClinicalClassifyActivity extends BaseActivity {
    private TagFlowLayout flowlayout_classify;
    private TextView tv;
    @Override
    protected void setView() {
        setContentView(R.layout.popupview_clinical_classify);
    }

    @Override
    protected void initView() {
        flowlayout_classify = (TagFlowLayout) findViewById(R.id.flowlayout_classify);

    }

    private String[] hotsearch = new String[]{"我们0","我们1","我们2","我们3","我们4","我们5","我们6","我们7", "我们0我们","我们1","我们2我们","我们3","我们4我们","我们5","我们6我们","我们7", "我们0","我们1我们啊","我们2","我们3我们啊","我们4","我们5我们啊","我们6","我们7我们啊"};
    @Override
    protected void initData() {
        final LayoutInflater mInflater = LayoutInflater.from(this);
        flowlayout_classify.setAdapter(new TagAdapter<String>(hotsearch) {

            @Override
            public View getView(com.android.linglan.widget.flowlayout.FlowLayout parent, int position, String s) {
                LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.item_clinical_classify,
                        flowlayout_classify, false);
                tv = (TextView) ll.findViewById(R.id.tv_hot_search_context);
                tv.setText(s);
                tv.setTextSize(16);
                Resources resource = getResources();
                ColorStateList csl = resource.getColorStateList(R.color.no_text_color_fragment_title);
                tv.setTextColor(csl);
                return ll;
            }
        });

        flowlayout_classify.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, com.android.linglan.widget.flowlayout.FlowLayout parent) {
                ToastUtil.show(hotsearch[position]);
//                view.setBackgroundResource(R.drawable.bg_bottom_textview);

                return true;
            }
        });

    }

    @Override
    protected void setListener() {

    }
}
