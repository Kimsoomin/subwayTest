package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.trend.TrendActivity;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.controllers.mainmenu.WishListAdapter;
import com.dabeeo.hangouyou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.utils.SystemUtil;
import com.dabeeo.hangouyou.views.PopularWishListParticleView;

public class WishListFragment extends Fragment
{
	private LinearLayout popularWishListContainer;
	private LinearLayout emptyContainer;
	private GridViewWithHeaderAndFooter listView;
	private WishListAdapter adapter;
	private Button btnPopularProduct;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		int resId = R.layout.fragment_wish_list;
		View view = inflater.inflate(resId, null);
		
		emptyContainer = (LinearLayout) view.findViewById(R.id.empty_container);
		btnPopularProduct = (Button) view.findViewById(R.id.btn_go_to_popular_product);
		btnPopularProduct.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!SystemUtil.isConnectNetwork(getActivity()))
					new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
				else
					startActivity(new Intent(getActivity(), TrendActivity.class));
			}
		});
		popularWishListContainer = (LinearLayout) view.findViewById(R.id.wish_list_container);
		PopularWishListParticleView pView = new PopularWishListParticleView(getActivity());
		pView.setBean(0, "CELDREAM 화장품", "아쿠아 리움 티켓");
		popularWishListContainer.addView(pView);
		
		pView = new PopularWishListParticleView(getActivity());
		pView.setBean(1, "같은 시간 속의 너", "W DRESSROOM 퍼퓸");
		popularWishListContainer.addView(pView);
		
		pView = new PopularWishListParticleView(getActivity());
		pView.setBean(2, "남성BB크림", "SKII 여성용 스킨세트");
		popularWishListContainer.addView(pView);
		
		listView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.listview);
		adapter = new WishListAdapter(getActivity(), new WishListListener()
		{
			@Override
			public void onRemove()
			{
				if (adapter.getCount() == 0)
				{
					emptyContainer.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
				else
				{
					emptyContainer.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
			}
		});
		listView.setAdapter(adapter);
		
		ProductBean bean = new ProductBean();
		bean.title = "[숨]워터풀 타임리스 워터젤 크림";
		bean.originalPrice = 80000;
		bean.discountPrice = 452000;
		adapter.add(bean);
		
		bean = new ProductBean();
		bean.title = "[SKII]나이트밤";
		bean.originalPrice = 100000;
		bean.discountPrice = 252000;
		adapter.add(bean);
		
		return view;
	}
}
