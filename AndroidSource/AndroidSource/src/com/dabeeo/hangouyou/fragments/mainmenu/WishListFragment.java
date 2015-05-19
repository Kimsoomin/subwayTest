package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.views.PopularWishListParticleView;

public class WishListFragment extends Fragment
{
	private LinearLayout popularWishListContainer;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		int resId = R.layout.fragment_wish_list;
		View view = inflater.inflate(resId, null);
		
		popularWishListContainer = (LinearLayout) view.findViewById(R.id.wish_list_container);
		for (int i = 0; i < 3; i++)
		{
			PopularWishListParticleView pView = new PopularWishListParticleView(getActivity());
			if (i == 1)
				pView.setBean(i, "TEST! WISHLIST!", "WISHER");
			else
				pView.setBean(i, "wishlist " + i, "wishlist " + i);
			
			popularWishListContainer.addView(pView);
		}
		return view;
	}
	
}
