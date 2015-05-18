/**
 * 
 */
package com.dabeeo.hangouyou.map;

import android.os.AsyncTask;

/**
 * @author 	: HyeongCheol.Son
 * @작성일   	: 2015. 4. 24. 
 * @param  	:
 * @Method 설명 :
 *
 */

public class MarkerSetAsyncTask extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	Context mContext;
//	BlinkingMap map;
//	
//	BoundedMapView m_mapView;
//	List<PlaceInfo> allplaceinfo;
//	ArrayList<OverlayItem> items;
//	
//	
//	
//	ImageView lineNumImage1;
//	ImageView lineNumImage2;
//	ImageView lineNumImage3;
//	ImageView lineNumImage4;
//	
//	TextView lineNumText1;
//	TextView lineNumText2;
//	TextView lineNumText3;
//	TextView lineNumText4;
//	
//	TextView summaryTitle02;
//	ImageView lineNumImage01;
//	ImageView lineNumImage02;
//	ImageView lineNumImage03;
//	ImageView lineNumImage04;
//	TextView lineNumText01;
//	TextView lineNumText02;
//	TextView lineNumText03;
//	TextView lineNumText04;
//	
//	// - 고유 생성 변수
//	List<PlaceInfo> onePlaceInfo;
//	ArrayList<OverlayItem> oneItems;
//	OnePlaceOverlay onePlaceOverlay;
//	
//	
//	
//	public MarkerSetAsyncTask(Context context,BlinkingMap map,BoundedMapView m_mapView) {
//		this.mContext = context;
//		this.map = map;
//		this.m_mapView = m_mapView;
//		
//		allplaceinfo = map.allplaceinfo;
//		items = map.items;
//		
//		
//		lineNumImage1 = map.lineNumImage1;
//		lineNumImage2 = map.lineNumImage2;
//		lineNumImage3 = map.lineNumImage3;
//		lineNumImage4 = map.lineNumImage4;
//		lineNumText1 = map.lineNumText1;
//		lineNumText2 = map.lineNumText2;
//		lineNumText3 = map.lineNumText3;
//		lineNumText4 = map.lineNumText4;
//		
//		
//		summaryTitle02 = map.summaryTitle02;
//		lineNumImage01 = map.lineNumImage01;
//		lineNumImage02 = map.lineNumImage02;
//		lineNumImage03 = map.lineNumImage03;
//		lineNumImage04 = map.lineNumImage04;
//		lineNumText01 = map.lineNumText01;
//		lineNumText02 = map.lineNumText02;
//		lineNumText03 = map.lineNumText03;
//		lineNumText04 = map.lineNumText04;
//	}
//	
//	
//	
//	TextView[] text = new TextView[4];
//	ImageView[] linenum = new ImageView[4];
//	@Override
//	protected Void doInBackground(Void... params) {
//		// TODO Auto-generated method stub
//		if (map.lineId != null && !map.lineId.equals("")) {
//			MapPlaceDataManager.getInstance(mContext).initDatabase();
//			onePlaceInfo = MapPlaceDataManager.getInstance(mContext).getPlacefromIdx(map.lineId);
//			OverlayItem oneItem = 
//					new OverlayItem("" + onePlaceInfo.get(0).m_nCategoryID,
//					onePlaceInfo.get(0).m_strName,
//					onePlaceInfo.get(0).m_nID, 
//					new GeoPoint(onePlaceInfo.get(0).m_fLatitude,onePlaceInfo.get(0).m_fLongitude));
//			oneItems = new ArrayList<OverlayItem>();
//			oneItems.add(oneItem);
//		} else {
//			Log.i("INFO", "lineID null");
//			
////			allplaceinfo.add((PlaceInfo) premiumInfo);
//			if (allplaceinfo.size() > items.size()) {
//				for (int i = 0; allplaceinfo.size() > i; i++) {
//					OverlayItem item = new OverlayItem(""+ allplaceinfo.get(i).m_nCategoryID,
//							allplaceinfo.get(i).m_strName,
//							allplaceinfo.get(i).m_nID, 
//							new GeoPoint(allplaceinfo.get(i).m_fLatitude,allplaceinfo.get(i).m_fLongitude));
//					
//					item.setMarker(Global.GetDrawable(mContext,R.drawable.transparent));
//					items.add(item);
//				}
//			}
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(Void result) {
//		super.onPostExecute(result);
//		if (map.lineId != null && !map.lineId.equals("")) {
//			onePlaceOverlay = new OnePlaceOverlay(oneItems, null,
//					new DefaultResourceProxyImpl(mContext), mContext);
//			m_mapView.getOverlays().add(onePlaceOverlay);
//			summaryTitle02.setText(onePlaceInfo.get(0).m_strName);
//			text[0] = lineNumText01;
//			text[1] = lineNumText02;
//			text[2] = lineNumText03;
//			text[3] = lineNumText04;
//			linenum[0] = lineNumImage01;
//			linenum[1] = lineNumImage02;
//			linenum[2] = lineNumImage03;
//			linenum[3] = lineNumImage04;
//			subwaylineNumSet(onePlaceInfo.get(0).m_nID);
//			summaryViewVisibleSet(summaryViewVisible, 3);
//		} else {
//			if (allplaceOverlay == null) {
//				text[0] = lineNumText1;
//				text[1] = lineNumText2;
//				text[2] = lineNumText3;
//				text[3] = lineNumText4;
//				linenum[0] = lineNumImage1;
//				linenum[1] = lineNumImage2;
//				linenum[2] = lineNumImage3;
//				linenum[3] = lineNumImage4;
//				allplaceOverlay = new PlaceOverlay(
//						items,
//						new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//							@Override
//							public boolean onItemLongPress(int arg0,
//									OverlayItem arg1) {
//								// TODO Auto-generated method stub
//								return false;
//							}
//
//							@Override
//							public boolean onItemSingleTapUp(int arg0,
//									OverlayItem arg1) {
//								// TODO Auto-generated method stub
//								m_mapView.getController().setCenter(
//										arg1.mGeoPoint);
//								idx = allplaceinfo.get(arg0).m_nID;
//								subway_m_fLatitude = allplaceinfo.get(arg0).m_fLatitude;
//								subway_m_fLongitude = allplaceinfo.get(arg0).m_fLongitude;
//								m_locMarkTarget = new Location("MarkTaget");
//								m_locMarkTarget.setLatitude(subway_m_fLatitude);
//								m_locMarkTarget.setLongitude(subway_m_fLongitude);
//								DestinationTitle = arg1.mTitle;
//								if (allplaceinfo.get(arg0).m_nCategoryID == 99) {
//									summaryTitle2.setText(DestinationTitle);
//									subwaylineNumSet(idx);
//									summaryViewVisibleSet(summaryViewVisible, 2);
//								} else {
//									summaryTitle.setText(DestinationTitle);
//									summarysubTitle.setText(allplaceinfo.get(arg0).m_strAddress);
//									summaryViewVisibleSet(summaryViewVisible, 1);
//								}
//								allplaceOverlay.changeMethod(
//										(BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),
//										idx);
//								return false;
//							}
//						}, new DefaultResourceProxyImpl(mContext), mContext, markerBitmap);
//			}else if(premiumOverlay == null)
//			{
//				premiumOverlay = new PlaceOverlay(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//
//					@Override
//					public boolean onItemLongPress(int arg0,
//							OverlayItem arg1) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//
//					@Override
//					public boolean onItemSingleTapUp(int arg0,
//							OverlayItem arg1) {
//						// TODO Auto-generated method stub
//						idx = premiumInfo.get(arg0).m_nID;
//						subway_m_fLatitude = premiumInfo.get(arg0).m_fLatitude;
//						subway_m_fLongitude = premiumInfo.get(arg0).m_fLongitude;
//						m_locMarkTarget = new Location("MarkTaget");
//						m_locMarkTarget.setLatitude(subway_m_fLatitude);
//						m_locMarkTarget.setLongitude(subway_m_fLongitude);
//						DestinationTitle = arg1.mTitle;
//						summaryTitle.setText(DestinationTitle);
//						summarysubTitle.setText(premiumInfo.get(arg0).m_strAddress);
//						summaryViewVisibleSet(summaryViewVisible, 1);
//						
//						premiumOverlay.changeMethod( (BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),idx);
//						return false;
//					}
//				}, new DefaultResourceProxyImpl(mContext), mContext, markerBitmap);
//			}
//
////			topLeftGpt = (GeoPoint) m_mapView.getProjection().fromPixels(40,
////					40);
////			bottomRightGpt = (GeoPoint) m_mapView
////					.getProjection()
////					.fromPixels(m_mapView.getWidth()-40, m_mapView.getHeight()-40);
////			allplaceOverlay.mapBoundaryCal(topLeftGpt, bottomRightGpt);
//			
//			m_mapView.getOverlays().add(allplaceOverlay);
//		}
//		runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				m_mapView.invalidate();
//			}
//		});
//	}
//
//	public void subwaylineNumSet(String lineid) {
//		summarysubTitle2.setVisibility(View.INVISIBLE);
//		String[] line = lineid.split("line");
//		
//		text[0].setVisibility(View.INVISIBLE);
//		text[1].setVisibility(View.INVISIBLE);
//		text[2].setVisibility(View.INVISIBLE);
//		text[3].setVisibility(View.INVISIBLE);
//		linenum[0].setVisibility(View.INVISIBLE);
//		linenum[1].setVisibility(View.INVISIBLE);
//		linenum[2].setVisibility(View.INVISIBLE);
//		linenum[3].setVisibility(View.INVISIBLE);
//		int lineLength = line.length - 1;
//
//		for (int i = 0; lineLength > i; i++) {
//			char a = line[i + 1].charAt(0);
//
//			if (a == '1') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line1);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("1호선");
//			} else if (a == '2') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line2);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("2호선");
//			} else if (a == '3') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line3);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("3호선");
//			} else if (a == '4') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line4);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("4호선");
//			} else if (a == '5') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line5);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("5호선");
//			} else if (a == '6') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line6);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("6호선");
//			} else if (a == '7') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line7);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("7호선");
//			} else if (a == '8') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line8);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("8호선");
//			} else if (a == '9') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_line9);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("9호선");
//			} else if (a == 'd') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_linesb);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("분당선");
//			} else if (a == 'p') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_linegc);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("경춘선");
//			} else if (a == 'a') {
//				linenum[i].setVisibility(View.VISIBLE);
//				linenum[i].setImageResource(R.drawable.icon_subway_linea);
//				text[i].setVisibility(View.VISIBLE);
//				text[i].setText("공항철도");
//			} else if (a == 'k') {
//				char b = line[i + 1].charAt(1);
//				if (b == '2') {
//					linenum[i]
//							.setImageResource(R.drawable.icon_subway_lineb);
//					text[i].setText("분당선");
//				} else {
//					linenum[i]
//							.setImageResource(R.drawable.icon_subway_linegj);
//					text[i].setText("경의중앙선");
//				}
//				text[i].setVisibility(View.VISIBLE);
//				linenum[i].setVisibility(View.VISIBLE);
//			}
//		}
//	}
}
