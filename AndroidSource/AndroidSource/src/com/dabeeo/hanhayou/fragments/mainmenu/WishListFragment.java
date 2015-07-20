package com.dabeeo.hanhayou.fragments.mainmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.WishListSearchActivity;
import com.dabeeo.hanhayou.activities.trend.TrendActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.PopularWishListParticleView;

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
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_wishlist, null);
		((Button) v.findViewById(R.id.btn_add_wishlist)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!SystemUtil.isConnectNetwork(getActivity()))
					new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
				else
					startActivity(new Intent(getActivity(), WishListSearchActivity.class));
			}
		});
		listView.addFooterView(v);
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
		bean.name = "[숨]워터풀 타임리스 워터젤 크림";
		bean.priceSale = "80000";
		bean.priceDiscount = "452000";
		adapter.add(bean);
		
		bean = new ProductBean();
		bean.name = "[SKII]나이트밤";
		bean.priceSale = "100000";
		bean.priceDiscount = "252000";
		adapter.add(bean);
		
		return view;
	}
}
