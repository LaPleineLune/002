package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.TCMSearchDetailsActivity;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.TextUtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 搜索返回结果的列表
 */
public class TCMSearchRequestResultListAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<Map<String, String>> mTCMSearchRows;
    private String key;
    public TCMSearchRequestResultListAdapter(Activity context, List<Object> searchRows ,String key) {
        this.context = context;
        this.key = key;
        List<Map<String, String>> rows = new ArrayList<>();
        if(searchRows != null) {
            for (int i = 0; i < searchRows.size(); ++i) {
                Map<String, Object> tmp = (Map<String, Object>) searchRows.get(i);
                if(tmp != null) {
                    List<Map<String, String>> rowsT = (List<Map<String, String>>) tmp.get("rows");
                    if (rowsT != null) {
                        for (int j = 0; j < rowsT.size(); j++) {
                            Map<String, String> td = rowsT.get(j);
                            if(td != null) {
                                td.put("page", (String) tmp.get("page"));
                                rows.add(td);
                            }
                        }
                    }
                }
            }
        }
        this.mTCMSearchRows = rows;
//        LogUtil.e("我要看的Map" + mTCMSearchRows.get("title"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_tcm_search, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.mTCMSearchRows != null ? this.mTCMSearchRows.size() : 0 ;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    public void updateAdapter(ArrayList<TCMSearchBean.TCMSearchData.TCMSearchRows> mTCMSearchRows){
//        this.mTCMSearchRows = mTCMSearchRows;
//        this.notifyDataSetChanged();
//    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_tcm_search_title;
//        private WebView webview_tcm_search_content;
        private TextView tv_tcm_search_content;
        private TextView tv_tcm_search_source;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_tcm_search_title = (TextView) rootView.findViewById(R.id.tv_tcm_search_title);
//            webview_tcm_search_content = (WebView) rootView.findViewById(R.id.webview_tcm_search_content);
            tv_tcm_search_content = (TextView) rootView.findViewById(R.id.tv_tcm_search_content);
            tv_tcm_search_source = (TextView) rootView.findViewById(R.id.tv_tcm_search_source);
        }
        public void bindData(final int position) {
            final String title = mTCMSearchRows.get(position).get("title" );
            if (title != null && !title.equals("")) {
                SpannableStringBuilder textString = TextUtilTools.highlight(title, key);
//                tv_tcm_search_title.setText(title);
                tv_tcm_search_title.setText(textString);
            } else {
                tv_tcm_search_title.setVisibility(View.GONE);
            }
            final String content = mTCMSearchRows.get(position).get("abstract");
            if (content != null && !content.equals("")) {
//                webview_tcm_search_content.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
//                tv_tcm_search_content.setText(Html.fromHtml(content, null, null));
                SpannableStringBuilder textString = TextUtilTools.highlight(content, key);
                tv_tcm_search_content.setText(textString);
            } else {
//                webview_tcm_search_content.setVisibility(View.GONE);
                tv_tcm_search_content.setVisibility(View.GONE);
            }
//            tv_tcm_search_content.setText(content);
            final String source = mTCMSearchRows.get(position).get("source");
            if (source != null && !source.equals("")) {
                tv_tcm_search_source.setText(source);
            } else {
                tv_tcm_search_source.setVisibility(View.GONE);
            }
//            tv_tcm_search_source.setText(mTCMSearchRows.get(position).get("id"));
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("key", key);
//                    intent.putExtra("page", page);
                    intent.putExtra("page", mTCMSearchRows.get(position).get("page"));
                    intent.putExtra("itemid", mTCMSearchRows.get(position).get("id"));
                    LogUtil.e("mTCMSearchRows.get(position).get(\"id\")" + mTCMSearchRows.get(position).get("id"));
                    intent.setClass(context, TCMSearchDetailsActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}