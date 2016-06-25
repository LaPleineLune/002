package com.android.linglan.ui.fm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.baidu.cyberplayer.download.VideoDownloadManager;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LeeMy on 2016/6/8 0008.
 * 音频批量下载页面
 */
public class RadioBatchDownloadActivity extends BaseActivity {
    private PtrClassicFrameLayout recycler_view_radio_special;
    private RecyclerView rec_radio_special;
    private ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
    private RecyclerAdapterWithHF mAdapter;
    private RadioBatchDownloadAdapter adapter;
    private int page;
    private String albumid;
    private HashMap<Integer, Boolean> isSelected;
    private VideoDownloadManager videoDownloadManager;

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
        setTitle("电台", "立即下载");
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
                 videoDownloadManager = VideoDownloadManager.getInstance(RadioBatchDownloadActivity.this, "com.android.linglan.ui");
                ProgressBarasyncTask asyncTask=new ProgressBarasyncTask();
                asyncTask.execute(1000);

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

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioBatchDownloadActivity.this)) {
                    recycler_view_radio_special.loadMoreComplete(false);
                    return;
                }
                RadioSpecialListBean radioSpecialListBean = JsonUtil.json2Bean(result, RadioSpecialListBean.class);
                if (page == 1) {
                    radioSpecialListData.clear();
                }
                radioSpecialListData.addAll(radioSpecialListBean.data);
                if (radioSpecialListData != null && radioSpecialListData.size() != 0) {
                    adapter.updateAdapter(radioSpecialListData);
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
                        R.layout.item_radio_batch_download_title, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(
                        R.layout.item_radio_batch_download, parent, false);
            }
            return new RadioPlaySpecialViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RadioPlaySpecialViewHolder) holder).bindData(position);
        }

        @Override
        public int getItemCount() {
            return this.radioSpecialListData != null ? this.radioSpecialListData.size() + 1 : 1;
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
            protected static final int REQUEST_CANCLE_SELECTED = 0;
            protected static final int REQUEST_COLLECT = 1;


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
                        tv_cancel_select.setText("全选");
                    }
                    if (radioSpecialListData != null) {
                        tv_count_view.setText("共" + radioSpecialListData.size() + "集");
                        tv_cancel_select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((tv_cancel_select.getText().toString().trim()).equals("全选")) {
                                    for (int i = 0; i < radioSpecialListData.size(); i++) {
                                        getIsSelected().put(i, true);
                                    }
                                    tv_cancel_select.setText("取消全选");
                                } else if ((tv_cancel_select.getText().toString().trim()).equals("取消全选")) {
                                    for (int i = 0; i < radioSpecialListData.size(); i++) {
                                        getIsSelected().put(i, false);
                                    }
                                    tv_cancel_select.setText("全选");
                                }
                                notifyDataSetChanged();

//                            for (int i = 0; i < radioSpecialListData.size(); i++) {
//                                if((tv_cancel_select.getText().toString().trim()).equals("全选")){
//                                    getIsSelected().put(i, true);
//
//                                }else if((tv_cancel_select.getText().toString().trim()).equals("取消全选")){
//                                    getIsSelected().put(i, false);
//
//                                }
//                            }

                            }
                        });
                    }

                    return;
                }

                if (radioSpecialListData != null && getIsSelected().size() != 0) {
                    iv_radio_download.setSelected(getIsSelected().get(position - 1));
                }

                tv_radio_download_title.setText(radioSpecialListData.get(position - 1).title);

                iv_radio_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iv_radio_download.isSelected()) {
                            iv_radio_download.setSelected(false);
                        } else {
                            iv_radio_download.setSelected(true);
                        }
                    }
                });

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

    public class ProgressBarasyncTask extends AsyncTask<Integer, Integer, String> {
//        private TextView tv;
//        private ProgressBar pb;
        public ProgressBarasyncTask() {
        }

        //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        //主要用于进行异步操作
        @Override
        protected String doInBackground(Integer... params) {
//            NetOperator netOperator =new NetOperator();
//            int i=0;
//            for ( i=10; i <=100; i+=10) {
//                netOperator.operate();
//                //执行publishProgress()调用onProgressUpdate()方法
//                publishProgress(i);
//            }
//            return i+params[0].intValue()+"";
            return "";
        }

        //该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
        @Override
        protected void onPreExecute() {
//            tv.setText("开始执行异步操作！");
            videoDownloadManager.startOrResumeDownloader("", null);
        }

        //在doInBackground方法当中，每次调用publishProgrogress()方法之后，都会触发该方法
        @Override
        protected void onProgressUpdate(Integer... values) {
            int value=values[0];
//            pb.setProgress(value);
        }
        //在doInBackground方法执行结束后再运行，并且运行在UI线程当中
        //主要用于将异步操作任务执行的结果展示给用户
        @Override
        protected void onPostExecute(String result) {
//            tv.setText("异步操作执行结束"+result);
        }

    }


}
