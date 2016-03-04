package com.android.linglan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class HistorySearchAdapter extends
        RecyclerView.Adapter{
    private Context context;
    public String[] historysearch;
   
    public HistorySearchAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_history_search_context, parent, false);
        return new HistorySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((HistorySearchViewHolder) holder).bindData((historysearch[position]));
        ((HistorySearchViewHolder) holder).bindData(position);
        LogUtil.e("onBindViewHolder＋＋＋" +historysearch[position]);
//        LogUtil.e("onBindViewHolder＋＋＋" +position);
    }

    @Override
    public int getItemCount() {
        return historysearch.length;
//        return 5;
    }

    public void upDate(String[] historysearch) {
        this.historysearch = historysearch;
        notifyDataSetChanged();
    }

    class HistorySearchViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;

        private TextView tv_item_hot_search;
        private String  key_world;

        public HistorySearchViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            tv_item_hot_search = (TextView) rootView.findViewById(R.id.tv_item_history_search);

//            rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtil.show(key_world);
//                    Intent intent = new Intent(context,
//                            SubjectDetailsActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }

//        private void bindData(String  key) {
////            this.key_world = historysearch[index];
//            tv_item_hot_search.setText(key);
//            LogUtil.e("bindData+++++"+ key);
//        }

        private void bindData(int  key) {
            this.key_world = historysearch[key];
            tv_item_hot_search.setText( historysearch[key].toString());
            LogUtil.e("bindData+++++"+ key_world);
        }


        @Override
        public void onClick(View v) {

        }
    }
}
