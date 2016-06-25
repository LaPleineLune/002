package com.android.linglan.ui.me;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioSpecialListBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LeeMy on 2016/6/8 0008.
 * 我的下载页面的音频列表
 */
public class RadioDownloadActivity extends BaseActivity {
    private PtrClassicFrameLayout recycler_view_radio_special;
    private RecyclerView rec_radio_special;
    private ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
    private RecyclerAdapterWithHF mAdapter;
    private RadioBatchDownloadAdapter adapter;
    private int page;
    private String albumid;
    private HashMap<Integer, Boolean> isSelected;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_radio_batch_download);
    }

    @Override
    protected void initView() {
        recycler_view_radio_special = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_radio_special);
        rec_radio_special = (RecyclerView) findViewById(R.id.rec_radio_special);
    }

    @Override
    protected void initData() {
        setTitle("电台", "删除");
        albumid = getIntent().getStringExtra("albumid");
        page = 1;
        radioSpecialListData = new ArrayList<RadioSpecialListBean.RadioSpecialListData>();
        rec_radio_special.setLayoutManager(new LinearLayoutManager(this));
        rec_radio_special.setHasFixedSize(true);
        adapter = new RadioBatchDownloadAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_radio_special.setAdapter(mAdapter);
        getRadioSpecialList(albumid, page);
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_radio_special.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                recycler_view_radio_special.refreshComplete();
                recycler_view_radio_special.setLoadMoreEnable(true);
                recycler_view_radio_special.loadMoreComplete(true);
                page = 1;
                getRadioSpecialList(albumid, page);
            }
        });

        //上拉刷新
        recycler_view_radio_special.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getRadioSpecialList(albumid, page);
                recycler_view_radio_special.loadMoreComplete(true);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("开始下载");
                //开始下载任务，需要下载的资源的
//                startOrResumeDownloader("",null);
//                VideoDownloadManager.getInstance(context, userName);
            }
        });
    }

    /**
     * 专辑详情列表
     * url
     *
     * @param cateid
     * @param page
     */
    private void getRadioSpecialList(final String cateid, final int page) {
        NetApi.getRadioSpecialList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_radio_special.refreshComplete();
                recycler_view_radio_special.setLoadMoreEnable(true);

                LogUtil.e("getRadioSpecialList=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioDownloadActivity.this)) {
                    recycler_view_radio_special.loadMoreComplete(false);
                    return;
                }
                RadioSpecialListBean radioSpecialListBean = JsonUtil.json2Bean(result, RadioSpecialListBean.class);
                if (page == 1) {
                    radioSpecialListData.clear();
                }
                radioSpecialListData.addAll(radioSpecialListBean.data);
                if (radioSpecialListData != null && radioSpecialListData.size() != 0) {
//                    adapter.updateAdapter(radioSpecialListData);
                }
//                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_radio_special.refreshComplete();
//                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, cateid, page + "");
    }

    public class RadioBatchDownloadAdapter extends RecyclerView.Adapter {
        private Context context;
        private Intent intent;
        public ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
        private String albumid;

        public RadioBatchDownloadAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == 0) {
                view = LayoutInflater.from(context).inflate(
                        R.layout.item_radio_download_title, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(
                        R.layout.item_radio_download, parent, false);
            }
            return new RadioPlaySpecialViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RadioPlaySpecialViewHolder) holder).bindData(position);
        }

        @Override
        public int getItemCount() {
            return this.radioSpecialListData != null ? this.radioSpecialListData.size() + 1 : 3;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void updateAdapter(ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData) {
            this.radioSpecialListData = radioSpecialListData;
            this.notifyDataSetChanged();
        }

        class RadioPlaySpecialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private View rootView;
            private ImageView iv_radio_download;
            private TextView tv_radio_download_title, tv_cancel_select, tv_count_view;


            public RadioPlaySpecialViewHolder(View rootView) {
                super(rootView);

                initView(rootView);
            }

            private void initView(View rootView) {
                this.rootView = rootView;
                isSelected = new HashMap<Integer, Boolean>();

                tv_radio_download_title = (TextView) rootView.findViewById(R.id.tv_radio_download_title);
                iv_radio_download = (ImageView) rootView.findViewById(R.id.iv_radio_download);

                tv_cancel_select = (TextView) rootView.findViewById(R.id.tv_cancel_select);
                tv_count_view = (TextView) rootView.findViewById(R.id.tv_count_view);

                if (radioSpecialListData != null && isSelected.size() == 0) {
                    for (int i = 0; i < radioSpecialListData.size(); i++) {
                        getIsSelected().put(i, false);
                    }
                }
            }

            public void bindData(final int position) {
                intent = new Intent();

                if (position == 0) {
                    if (isSelected.size() == 0) {
                        tv_cancel_select.setText("正在下载8个文件");
                    }
                    if (radioSpecialListData != null) {
//                        tv_count_view.setText("共" + radioSpecialListData.size() + "集");
//                        tv_cancel_select.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if ((tv_cancel_select.getText().toString().trim()).equals("全选")) {
//                                    for (int i = 0; i < radioSpecialListData.size(); i++) {
//                                        getIsSelected().put(i, true);
//                                    }
//                                    tv_cancel_select.setText("取消全选");
//                                } else if ((tv_cancel_select.getText().toString().trim()).equals("取消全选")) {
//                                    for (int i = 0; i < radioSpecialListData.size(); i++) {
//                                        getIsSelected().put(i, false);
//                                    }
//                                    tv_cancel_select.setText("全选");
//                                }
//                                notifyDataSetChanged();
//
//                            }
//                        });
                    }

                    return;
                }

//                if (radioSpecialListData != null && getIsSelected().size() != 0) {
//                    iv_radio_download.setSelected(getIsSelected().get(position - 1));
//                }
//
//                tv_radio_download_title.setText(radioSpecialListData.get(position - 1).title);
//
//                iv_radio_download.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (iv_radio_download.isSelected()) {
//                            iv_radio_download.setSelected(false);
//                        } else {
//                            iv_radio_download.setSelected(true);
//                        }
//                    }
//                });

            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }
//    @Override
//    protected void setView() {
//        setContentView(R.layout.activity_radio_download);
//    }
//
//    @Override
//    protected void initView() {
//        findViewById(R.id.tv_go_radio_details).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RadioDownloadActivity.this, RadioSpecialDetailsActivity.class));
//            }
//        });
//    }
//
//    @Override
//    protected void initData() {
//        setTitle("我的下载", "");
//    }
//
//    @Override
//    protected void setListener() {
//
//    }
}
