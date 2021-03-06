package com.dabeeo.hanhayou.map;

import java.io.File;

import org.osmdroid.tileprovider.MapTile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**9.Map 관련 DB Calss , osmdroid 에 맞게 데이터를 가공해서 사용하기 위해 SQLite에 저장.
 *	seoul_2013.02.19.mbtiles DB 이용한다.
 * */
@SuppressLint("DefaultLocale")
public class MapDatabase extends SQLiteOpenHelper
{

	public final String TAG = this.getClass().getName();
	private SQLiteDatabase m_db = null;

	static DrawableCache m_drawableCache = new DrawableCache();

	public MapDatabase(Context context)
	{
		//context, Database Name(seoul_2013.02.19.mbtiles) ,Factory , Database Version
		super(context, Global.g_strMapDBFileName, null, 1);

		m_db = OpenDatabase();
	}

	public MapDatabase(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}

	public SQLiteDatabase OpenDatabase()
	{
		//		String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		//		String strDBFilePath = sdCardPath + "/.BlinkingSeoul/" + Global.g_strMapDBFileName;
		try 
		{
			if(new File(Global.strMapDBFilePath).exists() == false)
			{
				Log.e("ERROR", "Can't open the MapDB: " + Global.strMapDBFilePath);
				return null;
			}

			return SQLiteDatabase.openDatabase(Global.strMapDBFilePath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

	//Tile 이 찾는 Map 을 DB에서 검색해서 이미지 리턴. 
	@SuppressWarnings("deprecation")
	public Drawable GetMapTile(int nZoomLevel, String strRow, int nColumn)
	{
		if(m_db == null)
		{
			Log.e("ERROR", "m_db == null");
			return null;
		}
		//		Log.d("DEBUG", "nZoomLevel : " + nZoomLevel +"," +"strRow : "+strRow+","+"nColumn: "+nColumn);

		try
		{
			String strQuery = String.format("select tile_data from tiles where zoom_level = %d and tile_row = %s and tile_column = %d limit 1;", nZoomLevel, strRow, nColumn);

			//			Log.d("DEBUG", "[Query] " + strQuery);

			Cursor cursor = m_db.rawQuery(strQuery, null);

			Drawable d = null;

			if(cursor.moveToNext())
			{
				byte[] b = cursor.getBlob(0);

				d = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
			}

			cursor.close();
			return d;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * XXX Get inputStream of tile from DB
	 */
	public Drawable getMapTile(final MapTile pTile) {
		if (m_db == null) {
			Log.e("ERROR", "m_db == null");
			return null;
		}

		try {
			final String[] tile = { "tile_data" };
			final String[] xyz = {
					Integer.toString(pTile.getX()),
					Double.toString(Math.pow(2, pTile.getZoomLevel())
							- pTile.getY() - 1),
							Integer.toString(pTile.getZoomLevel()) };
			Drawable d = null;

			final Cursor cursor = m_db.query("tiles", tile,
					"tile_column=? and tile_row=? and zoom_level=?", xyz, null,
					null, null);

			if (cursor.getCount() != 0) {
				// XXX testing lower quality for speed
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inPurgeable = true;
				opts.inDither = true;
				opts.inPreferredConfig = Bitmap.Config.RGB_565;

				cursor.moveToFirst();
				byte[] b = cursor.getBlob(0);
				d = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0,
						b.length, opts));
			}
			cursor.close();
			if (d != null) {
				return d;
			}
		} catch (final Throwable e) {
			Log.w(TAG, "Error getting tile: " + pTile, e);
		}

		return null;
	}

	@Override
	public synchronized void close()
	{
		if(m_db != null)
		{
			if(m_db.isOpen() == true)
				m_db.close();

			m_db = null;
		}

		super.close();
	}
}
