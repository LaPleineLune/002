package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCollatingActivity;
import com.android.linglan.ui.clinical.ClinicalManageClassifyActivity;
import com.android.linglan.ui.clinical.ClinicalReferenceActivity;
import com.android.linglan.ui.clinical.ClinicalSearchActivity;
import com.android.linglan.ui.clinical.TCMSearchActivity;
import com.android.linglan.ui.clinical.WeichatFlupActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.flowlayout.FlowLayout;
import com.android.linglan.widget.flowlayout.TagAdapter;
import com.android.linglan.widget.flowlayout.TagFlowLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 临症首页的Adapter
 */
public class ClinicalAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_TITLE = 0;
    static final int VIEW_CLINICAL_LIST = 1;
    private Activity context;
    private ClinicalListAdapter clinicalListAdapter;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private String collatingNum = "0";

    private PopupWindow popupWindow;
    private View popView;

    public ClinicalAdapter(Activity context) {
        this.context = context;
    }

    public void CollatingNum(String collatingNum) {
        this.collatingNum = collatingNum;
    }
    public void upDateAdapter(ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData, ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList) {
        this.clinicalCollatingData = clinicalCollatingData;
        this.clinicalCollatingList = clinicalCollatingList;
        notifyDataSetChanged();
    }

    // 临证的头部
    private View createClinicalTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_title, parent, false);
        popView = LayoutInflater.from(context).inflate(R.layout.popupview_clinical_classify, null);
        return view;
    }

    // 临证列表
    private View createClinicalListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_list, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_CLINICAL_TITLE:
                view = createClinicalTitleView(parent);
                break;
            case VIEW_CLINICAL_LIST:
                view = createClinicalListView(parent);
                break;
            default:
                break;
        }
        return new ClinicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ClinicalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ClinicalFragment clinicalFragment = new ClinicalFragment();
        private int classifyFlag1 = 0;
        private int classifyFlag2 = 0;
        private int positionFlag1 = -1;
        private int positionFlag2 = -1;
        private String tagid1 = "0";
        private String tagid2 = "0";

        private View rootView;
        private Intent intent = null;

        private Button btn_search;
        private TextView tv_clinical_reference;
        private TextView tv_weichat_flup;
        private TextView tv_tcm_search;
        private TextView tv_clinical_classify_type;
        private View view_collating;
        private LinearLayout ll_search;
        private LinearLayout ll_clinical_collating;
        private LinearLayout ll_clinical_classify;

        private LinearLayout ll_pop_clinical_classify;
        private LinearLayout ll_clinical_classify_title;
        private RelativeLayout rl_pop_clinical_classify;
        private TextView tv_classify_num;
        private TextView tv_classify_all_num;
        private TextView tv_classify_ok;
        private TextView tv_classify1;
        private TextView tv_classify2;
        private TextView tv_all_classify;
        private TagFlowLayout flowlayout_classify;
        private TextView tv;
        private TextView tv_manage_classify;

        private RecyclerView rec_clinical_list;

        private EditText filter_edit;

        public ClinicalViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            popupWindow = new PopupWindow(context);
            btn_search = (Button) rootView.findViewById(R.id.btn_search);
            tv_clinical_reference = (TextView) rootView.findViewById(R.id.tv_clinical_reference);
            tv_weichat_flup = (TextView) rootView.findViewById(R.id.tv_weichat_flup);
            tv_tcm_search = (TextView) rootView.findViewById(R.id.tv_tcm_search);
            tv_clinical_classify_type = (TextView) rootView.findViewById(R.id.tv_clinical_classify_type);
            view_collating = rootView.findViewById(R.id.view_collating);
            ll_search = (LinearLayout) rootView.findViewById(R.id.ll_search);
            ll_clinical_collating = (LinearLayout) rootView.findViewById(R.id.ll_clinical_collating);
            ll_clinical_classify = (LinearLayout) rootView.findViewById(R.id.ll_clinical_classify);

            ll_pop_clinical_classify = (LinearLayout) popView.findViewById(R.id.ll_pop_clinical_classify);
            ll_clinical_classify_title = (LinearLayout) popView.findViewById(R.id.ll_clinical_classify_title);
            rl_pop_clinical_classify = (RelativeLayout) popView.findViewById(R.id.rl_pop_clinical_classify);
            tv_classify_num = (TextView) popView.findViewById(R.id.tv_classify_num);
            tv_classify_all_num = (TextView) popView.findViewById(R.id.tv_classify_all_num);
            tv_classify_ok = (TextView) popView.findViewById(R.id.tv_classify_ok);
            tv_classify1 = (TextView) popView.findViewById(R.id.tv_classify1);
            tv_classify2 = (TextView) popView.findViewById(R.id.tv_classify2);
            tv_classify1.setText("");
            tv_classify2.setText("");
            tagid1 = "0";
            tagid2 = "0";
            tv_all_classify = (TextView) popView.findViewById(R.id.tv_all_classify);
            flowlayout_classify = (TagFlowLayout) popView.findViewById(R.id.flowlayout_classify);
            tv_manage_classify = (TextView) popView.findViewById(R.id.tv_manage_classify);

            rec_clinical_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_list);

            filter_edit = (EditText) rootView.findViewById(R.id.filter_edit);
        }

        private void bindData(int index) {
            intent = new Intent();
            switch (index) {
                case VIEW_CLINICAL_TITLE:
                    btn_search.setOnClickListener(this);
                    tv_clinical_reference.setOnClickListener(this);
                    tv_weichat_flup.setOnClickListener(this);
                    tv_tcm_search.setOnClickListener(this);
                    ll_search.setOnClickListener(this);
                    if (!collatingNum.equals("0")) {
                        ll_clinical_collating.setVisibility(View.VISIBLE);
                        view_collating.setVisibility(View.VISIBLE);
                    } else {
                        ll_clinical_collating.setVisibility(View.GONE);
                        view_collating.setVisibility(View.GONE);
                    }
                    ll_clinical_collating.setOnClickListener(this);
                    ll_clinical_classify.setOnClickListener(this);

                    ll_pop_clinical_classify.setOnClickListener(this);
                    tv_classify_ok.setOnClickListener(this);
                    tv_classify1.setOnClickListener(this);
                    tv_classify2.setOnClickListener(this);
                    tv_manage_classify.setOnClickListener(this);

                    tv_all_classify.setOnClickListener(this);

                    filter_edit.setOnClickListener(this);

                    flowlayout_classify.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, com.android.linglan.widget.flowlayout.FlowLayout parent) {
//                            if (ll_clinical_classify_title.isFocused())
                                ll_clinical_classify_title.setVisibility(View.GONE);

                            if (!rl_pop_clinical_classify.isFocused())
                                rl_pop_clinical_classify.setVisibility(View.VISIBLE);
                            if (positionFlag1 == -1 || positionFlag2 == -1) {
                                if ((positionFlag1 != position && positionFlag2 != position) && ((classifyFlag1 == 0 && classifyFlag2 == 0) || (classifyFlag1 == 0 && classifyFlag2 == 1))) {
                                    tv_classify1.setVisibility(View.VISIBLE);
//                                    tv_classify1.setText(hotsearch[position]);
//                                    tv_classify1.setText(clinicalClassifyData.get(position).tagname);
                                    tv_classify1.setText(data.get(position).tagname);
                                    tagid1 = data.get(position).tagid;
                                    classifyFlag1 = 1;
                                    positionFlag1 = position;
                                    if (classifyFlag1 == 0 && classifyFlag2 == 1) {
                                        tv_classify_num.setText("2个");
                                    } else {
                                        tv_classify_num.setText("1个");
                                    }
                                } else if ((positionFlag2 != position && positionFlag1 != position) && (classifyFlag1 == 1 && classifyFlag2 == 0)) {
                                    tv_classify2.setVisibility(View.VISIBLE);
//                                    tv_classify2.setText(hotsearch[position]);
//                                    tv_classify2.setText(clinicalClassifyData.get(position).tagname);
                                    tv_classify2.setText(data.get(position).tagname);
                                    tagid2 = data.get(position).tagid;
                                    classifyFlag2 = 1;
                                    positionFlag2 = position;
                                    tv_classify_num.setText("2个");
                                } else if (classifyFlag1 == 1 && classifyFlag2 == 1) {
                                    ToastUtil.show("最多只能选择两个标签");
                                } else if ((positionFlag2 == -1 && positionFlag1 == position) || (positionFlag1 == -1 && positionFlag2 == position)) {
                                    ToastUtil.show("请选择不同标签");
                                }
                            } else {
                                if (classifyFlag1 == 1 && classifyFlag2 == 1) {
                                    ToastUtil.show("最多只能选择两个标签");
                                } else {
                                    ToastUtil.show("请选择不同标签");
                                }
                            }
//                            ToastUtil.show(hotsearch[position]);
//                view.setBackgroundResource(R.drawable.bg_bottom_textview);

                            return true;
                        }
                    });

                    break;
                case VIEW_CLINICAL_LIST:
                    rec_clinical_list.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_clinical_list.setHasFixedSize(true);

//                    clinicalListAdapter = new SubjectDetailsListAdapter(context, subjectData);
                    clinicalListAdapter = new ClinicalListAdapter(context, clinicalCollatingData, clinicalCollatingList);
                    rec_clinical_list.setAdapter(clinicalListAdapter);
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_search:
                    Intent intent1 = new Intent(context,ClinicalSearchActivity.class);
                    context.startActivity(intent1);
                    break;
                case R.id.btn_search:
//                    ToastUtil.show("我是搜索");
                    String key = filter_edit.getText().toString().trim();
                    if(key != null && key.length() != 0){
                        Intent intent = new Intent(context,ClinicalSearchActivity.class);
                        intent.putExtra("key",key);
                        context.startActivity(intent);
                    }else{
                        ToastUtil.show("请输入搜索内容");
                    }

                    break;
                case R.id.tv_clinical_reference:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalReferenceSearch);
                    intent.setClass(context, ClinicalReferenceActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.tv_weichat_flup:
                    intent.setClass(context, WeichatFlupActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.tv_tcm_search:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalSelectSoso);
                    intent.setClass(context, TCMSearchActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.ll_clinical_collating:
                    intent.setClass(context, ClinicalCollatingActivity.class);
//                    intent.putExtra("collatingNum", collatingNum);
                    context.startActivity(intent);
                    break;
                case R.id.ll_clinical_classify:
                    ll_clinical_classify.setFocusable(false);
                    ll_clinical_classify.setClickable(false);
                    tv_classify1.setText("");
                    tv_classify2.setText("");
                    tagid1 = "0";
                    tagid2 = "0";
//                    getClinicalClassify(v);
                    showPopup(v);
                    break;
                case R.id.tv_manage_classify:
                    intent.setClass(context, ClinicalManageClassifyActivity.class);
                    context.startActivity(intent);
                    popupWindow.dismiss();
                    ll_clinical_classify.setFocusable(true);
                    ll_clinical_classify.setClickable(true);
                    break;
                case R.id.ll_pop_clinical_classify:
                    popupWindow.dismiss();
                    ll_clinical_classify.setFocusable(true);
                    ll_clinical_classify.setClickable(true);
                    break;
                case R.id.tv_classify1:
                    tv_classify1.setVisibility(View.GONE);
                    tv_classify1.setText("");
                    tagid1 = "0";
                    classifyFlag1 = 0;
                    positionFlag1 = -1;
                    tv_classify_num.setText("1个");
                    if (classifyFlag1 == 0 && classifyFlag2 == 0) {
                        ll_clinical_classify_title.setVisibility(View.VISIBLE);
                        rl_pop_clinical_classify.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_classify2:
                    tv_classify2.setVisibility(View.GONE);
                    tv_classify2.setText("");
                    tagid2 = "0";
                    classifyFlag2 = 0;
                    positionFlag2 = -1;
                    tv_classify_num.setText("1个");
                    if (classifyFlag1 == 0 && classifyFlag2 == 0) {
                        ll_clinical_classify_title.setVisibility(View.VISIBLE);
                        rl_pop_clinical_classify.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_classify_ok:
                    popupWindow.dismiss();
                    ll_clinical_classify.setFocusable(true);
                    ll_clinical_classify.setClickable(true);
                    String tagid = "";
                    if (!tv_classify1.getText().toString().equals("") && !tv_classify2.getText().toString().equals("")) {
                        tv_clinical_classify_type.setText(tv_classify1.getText().toString() + " + " + tv_classify2.getText().toString());
                        tagid = tagid1 + "," + tagid2;
                    } else {
                        tv_clinical_classify_type.setText(tv_classify1.getText().toString() + tv_classify2.getText().toString());
                        if (!tagid1.equals("0")) {
                            tagid = tagid1;
                        } else if (!tagid2.equals("0")) {
                            tagid = tagid2;
                        }
                    }
                    clinicalFragment.getClassify(tagid);
                    break;
                case R.id.tv_all_classify:
                    popupWindow.dismiss();
                    ll_clinical_classify.setFocusable(true);
                    ll_clinical_classify.setClickable(true);
                    tv_clinical_classify_type.setText("全部");
                    clinicalFragment.getAllClassify();
                    break;
                case R.id.filter_edit:

                    break;
            }
        }

        private void showPopup(View v) {
            getClinicalClassify();
            ll_clinical_classify_title.setVisibility(View.VISIBLE);
            rl_pop_clinical_classify.setVisibility(View.GONE);
            tv_classify1.setVisibility(View.GONE);
            tagid1 = "0";
            tagid2 = "0";
            classifyFlag1 = 0;
            classifyFlag2 = 0;
            positionFlag1 = -1;
            positionFlag2 = -1;
            tv_classify2.setVisibility(View.GONE);
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            popView.measure(w, h);

            int height = popView.getMeasuredHeight();
            int width = popView.getMeasuredWidth();
            popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);

//            popupWindow = new PopupWindow(popView, width, (int) height);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            int[] location = new int[2];
            v.getLocationOnScreen(location);

            int x = context.getResources().getDimensionPixelSize(R.dimen.dp14);
            int y = context.getResources().getDimensionPixelSize(R.dimen.dp14);

//            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - v.getWidth() + x, location[1] + v.getHeight() - y);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, getStatusBarHeight());

        }

        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        final LayoutInflater mInflater = LayoutInflater.from(context);
        final List<ClinicalClassifyBean.ClinicalClassifyData> data = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();
        private void getClinicalClassify() {
            NetApi.getClinicalClassify(new PasserbyClient.HttpCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("getClinicalClassify=" + result);
                    if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                        ll_clinical_classify.setFocusable(true);
                        ll_clinical_classify.setClickable(true);
                        return;
                    }

                    ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);

                    clinicalClassifyData = clinicalClassifyBean.data;

                    data.clear();
                    for(ClinicalClassifyBean.ClinicalClassifyData datas : clinicalClassifyData) {
                        if (!datas.userid.equals("0")) {
                            data.add(datas);
                        }
                    }
                    tv_classify_all_num.setText("共" + (data != null ? data.size() : 0) +"个分类");
//                    showPopup(v);

                    flowlayout_classify.setAdapter(new TagAdapter(data) {// clinicalClassifyData
                        @Override
                        public View getView(FlowLayout parent, int position, Object o) {
                            LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.item_clinical_classify,
                                    flowlayout_classify, false);
                            tv = (TextView) ll.findViewById(R.id.tv_classify_context);
                            tv.setText(data.get(position).tagname
                                    + "  " + data.get(position).cont);
                            tv.setTextSize(16);
                            Resources resource = context.getResources();
                            ColorStateList csl = resource.getColorStateList(R.color.french_grey);//no_text_color_fragment_title
                            tv.setTextColor(csl);
                            return ll;
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    ll_clinical_classify.setFocusable(true);
                    ll_clinical_classify.setClickable(true);
                }
            });
        }

    }
}
