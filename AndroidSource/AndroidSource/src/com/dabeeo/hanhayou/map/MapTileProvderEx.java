package com.dabeeo.hanhayou.map;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/** 10-1 Map tile 데이터 가공 > 이미지화 */
public class MapTileProvderEx extends MapTileProviderBase
{

	public final String TAG = this.getClass().getName();
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
		// Check database
		if (m_mapDB == null) {
			m_mapDB = new MapDatabase(m_context);
			if (m_mapDB == null) {
				Log.e("ERROR", "m_db == null");
				return null;
			}
		}

		// Try to load tile from cache
		Drawable d = this.mTileCache.getMapTile(pTile);

		// If not in cache, get it
		if (d == null) {
			int nZoomLevel = pTile.getZoomLevel();
			String strRow = Double.toString(Math.pow(2, pTile.getZoomLevel()) - pTile.getY() - 1);
			int nColumn = pTile.getX();
			// Check for zoom level and use another provider if out of the
			// bounds
			if (nZoomLevel >= getMaximumZoomLevel()) {
				// Use the method to rescale the tile to fake zoom
				Log.i(TAG, "Resizing, fake zoom");

				final int maxZoom = 2; // TODO adjust amount of fake Zoom
				MapTile rTile = null;
				Drawable fakeZoomDrawable;
				int rx, ry;
				for (int z = 1; z < maxZoom; z++) {
					rx = pTile.getX() / (2 * z);
					ry = pTile.getY() / (2 * z);
					rTile = new MapTile(nZoomLevel - z, rx, ry);

					fakeZoomDrawable = m_mapDB.getMapTile(rTile);

					if (fakeZoomDrawable != null) {
						
						d = resize(fakeZoomDrawable, pTile.getX(),
								pTile.getY(), z);
					} else {
						Log.d(TAG, "Tile doesn't exist. Can't find a tile to scale: "+ pTile);
					}
				}

			} else {
				// Use supported zoom level instead of fake zoom
				d = m_mapDB.GetMapTile(nZoomLevel, strRow, nColumn);
			}

			// Set loaded zoom to cache
			if (d != null)
				this.mTileCache.putTile(pTile, d);
		}

		// In case we can't get any tile, set the default image
		if (d == null)
			d = m_tile;
		
		return d;
	}

	// XXX Rescale bitmap
	private Drawable resize(Drawable image, final int rx, final int ry,
			final int zoomLevelDiff) {
		int mTileSizePixels = 128; // 256
		int px = rx % (2 * zoomLevelDiff);
		int py = ry % (2 * zoomLevelDiff);
		Bitmap b = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapResized = Bitmap.createBitmap(
				Bitmap.createScaledBitmap(b, (mTileSizePixels * 2) * zoomLevelDiff, (mTileSizePixels * 2) * zoomLevelDiff, false), 
				(px == 0) ? 0 : mTileSizePixels * px, 
						(py == 0) ? 0 : mTileSizePixels * py,
								mTileSizePixels, mTileSizePixels);

		return new BitmapDrawable(m_context.getResources(),
				bitmapResized);
	}

	@Override
	public int getMaximumZoomLevel() {
		return 18;
	}

	@Override
	public int getMinimumZoomLevel() {
		return 13;
	}

	@Override
	public boolean useDataConnection()
	{
		return false;
	}
}
