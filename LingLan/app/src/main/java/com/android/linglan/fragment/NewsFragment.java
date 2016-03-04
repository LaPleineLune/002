package com.android.linglan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.adapter.AllArticleAdapter;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

	private static final String ARG_POSITION = "position";
	private static final String ARG_CATEID = "cateid";
	private static final String ARG_ORDER = "order";

	private int position;
	private String cateid;
	private int page =1;
	private CustomPullToRefreshRecyclerView article_refresh;
	private RecyclerView rec_all_article;

	private View rootView;

	private AllArticleAdapter allArticleAdapter;
	private AllArticleClassifyListBean AllArticleClassifyList;
	private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> ArticleClassifyList;

	private String order;//排序方式('addtime按时间排序' ,'count_view按统计排序')

	public static NewsFragment newInstance(int position, String cateid,String order) {
		NewsFragment f = new NewsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putString(ARG_CATEID, cateid);
		b.putString(ARG_ORDER, order);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		cateid = getArguments().getString(ARG_CATEID);
		order = getArguments().getString(ARG_ORDER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.layout_newsfragment, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

//		FrameLayout fl = new FrameLayout(getActivity());
//		fl.setLayoutParams(params);

//		RecyclerView rec_all_article = new RecyclerView(getActivity());
		article_refresh =  (CustomPullToRefreshRecyclerView)rootView.findViewById(R.id. article_refresh);
//		RecyclerView rec_all_article = (RecyclerView) rootView.findViewById(R.id.rec_all_article_page);
		rec_all_article = article_refresh.getRefreshableView();
		rec_all_article.setLayoutManager(new SyLinearLayoutManager(getActivity()));
		rec_all_article .setHasFixedSize(true);

		getAllArticleClassifyList(cateid,page,order);

		allArticleAdapter = new AllArticleAdapter(getActivity());
		rec_all_article.setAdapter(allArticleAdapter);

		article_refresh.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
			@Override
			public void onPullDownToRefresh() {
				page = 1;
				getAllArticleClassifyList(cateid,page,order);
			}

			@Override
			public void onPullUpToLoadMore() {
				page++;
				LogUtil.e("页面数：" + page);
				getAllArticleClassifyList(cateid,page,order);
			}
		});


		return rootView;

//		fl.addView(rec_all_article);
//		return fl;
	}
   //得到所有的文章分类
	private void getAllArticleClassifyList(String cateid, final int page,String order) {
		LogUtil.e("全部文章分类列表id" + cateid);
		NetApi.getAllArticleClassifyList(new PasserbyClient.HttpCallback() {
			@Override
			public void onSuccess(String result) {
				article_refresh.onRefreshComplete();

				if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
					return;
				}

				AllArticleClassifyList = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
				if(page ==1){
					ArticleClassifyList = AllArticleClassifyList.data;
				}else{
					if(AllArticleClassifyList.data == null || (AllArticleClassifyList.data).size() == 0){
						ToastUtil.show("没有数据了");
					}else{
						ArticleClassifyList.addAll(AllArticleClassifyList.data);
					}
				}

				LogUtil.d("全部文章分类列表" + ArticleClassifyList.toString());
				if (ArticleClassifyList != null && !ArticleClassifyList.equals("")) {
					allArticleAdapter.updateAdapter(ArticleClassifyList);
				}
			}

			@Override
			public void onFailure(String message) {

			}
		}, cateid, page+"",order);
	}
	//文章分类位置变动后上传新的排序
	private void geArticleOrderEdit(String cateorder){
		NetApi.geArticleOrderEdit(new PasserbyClient.HttpCallback() {
			@Override
			public void onSuccess(String result) {
				LogUtil.e("url=" + result);

				if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
					return;
				}
			}

			@Override
			public void onFailure(String message) {

			}
		}, cateorder);
	}
}