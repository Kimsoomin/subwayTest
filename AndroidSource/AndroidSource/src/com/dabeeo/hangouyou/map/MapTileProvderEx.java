package com.dabeeo.hangouyou.map;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/** 10-1 Map tile 데이터 가공 > 이미지화 */
public class MapTileProvderEx extends MapTileProviderBase
{
	private MapDatabase m_mapDB = null;
	private Drawable m_tile = null;
	private Context m_context;
	
	public MapTileProvderEx(ITileSource pTileSource, Drawable pTile,Context c)
	{
		super(pTileSource);

//		m_mapDB = new MapDatabase(MainTabActivity.g_this);
		m_mapDB = new MapDatabase(c);
		m_tile = pTile;
		m_context = c;
	}

	//Map DB 종료.
	@Override
	public void detach()
	{
		Log.e("MapTileProvider", "detatch");
		
		if(m_mapDB != null)
		{
			m_mapDB.close();
			m_mapDB = null;
		}
		
//		BlinkingSeoulActivity.g_sensorcallback = null;
	}

	//MapTile을 Drawable(이미지화) 하는 로직.
	@Override
	public Drawable getMapTile(MapTile pTile)
	{
		if(m_mapDB == null)
		{
			m_mapDB = new MapDatabase(m_context);

			if(m_mapDB == null)
			{
				Log.e("ERROR", "m_db == null");
				return null;
			}
		}
		
		//로직이 hasTable 로 설정되어 있어 Tile을 보내면 해당 이미지를 리턴하게 되어 있다.
		Drawable d = this.mTileCache.getMapTile(pTile);
		
		if(d == null)
		{
			// DB에서 읽어들임. 
			int nZoomLevel = pTile.getZoomLevel();
			
			// strRow(타일의 Row값) = 타일의 줌 레벨을 제곱 - 타일의 Y 값 - 1 
			String strRow = Double.toString(Math.pow(2, pTile.getZoomLevel()) - pTile.getY() - 1);
			int nColumn = pTile.getX();

			d = m_mapDB.GetMapTile(nZoomLevel, strRow, nColumn);
			
			//맵의 이미지를 가져왔으면 MapTileCash에서 가져와 쓸수 있도록 Tile과 이미지를 setter에 재 설정 해줘야 함.
			if(d != null)
			{
				mTileCache.putTile(pTile, d);
			}
		}
		else 
		{
			Log.d("DEBUG", "");
		}

		//위의 로직을 거쳤는데도 불구하고 이미지를 불러오지 못한다면 Default 처리.
		if(d == null)
		{
			d = m_tile;
//			Log.i("MapTileProvider", "" + arg0 + "  drawable: " + d);
		}

		return d;
	}

	@Override
	public int getMaximumZoomLevel() 
	{
		return 18;
	}

	@Override
	public int getMinimumZoomLevel() 
	{
		return 14;
	}

	@Override
	public boolean useDataConnection()
	{
		return false;
	}
}
