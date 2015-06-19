package com.dabeeo.hanhayou.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/** 10-3 맵�?? ????????? ?????? CustomView�? ??��?��?? Layout??? �????�? ??????.
 * onLayout, onMeasure, onDraw �? ????????��?��?????. ??��????? LifeCycle
 * 
 * */
public class RotateViewGroup extends ViewGroup
{
	private static final float SQ2 = 1.414213562373095f;
	public float m_fAngle = 0;

	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int m_nTouchMode = NONE;

	public RotateViewGroup(Context context)
	{
		super(context);

		setBackgroundColor(Color.WHITE);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
//		canvas.save(Canvas.MATRIX_SAVE_FLAG);
//
//        int py=canvas.getClipBounds().bottom/2; 
//        int px=canvas.getClipBounds().right/2; 
//
//		canvas.rotate(-m_fAngle, px, py);
		super.dispatchDraw(canvas);
//		canvas.restore();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) 
	{
		final int width = getWidth();
		final int height = getHeight();
		final int count = getChildCount();
		for (int i = 0; i < count; i++) 
		{
			final View view = getChildAt(i);
			final int childWidth = view.getMeasuredWidth();
			final int childHeight = view.getMeasuredHeight();
			final int childLeft = (width - childWidth) / 2;
			final int childTop = (height - childHeight) / 2;
			view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
		}
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int sizeSpec;
		if (w > h) 
		{
			sizeSpec = MeasureSpec.makeMeasureSpec((int) (w * SQ2), MeasureSpec.EXACTLY);
		} else 
		{
			sizeSpec = MeasureSpec.makeMeasureSpec((int) (h * SQ2), MeasureSpec.EXACTLY);
		}
		
		final int count = getChildCount();
		for (int i = 0; i < count; i++) 
		{
			getChildAt(i).measure(sizeSpec, sizeSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void onAccuracyChanged(int sensor, int accuracy)
	{
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		try
		{
			if(Build.VERSION_CODES.GINGERBREAD_MR1 >= Build.VERSION.SDK_INT)
			{
				int act = event.getAction();

			    switch(act & MotionEvent.ACTION_MASK)
			    {
			        case MotionEvent.ACTION_DOWN:
			        	//첫�??�? ???�???? ??��??(??????�? ??��??)
			            Log.d("zoom", "mode=DRAG" );
			            m_nTouchMode = DRAG;
			            break;

			        case MotionEvent.ACTION_MOVE: 
			            if(m_nTouchMode != ZOOM)
			            {  // ??????�? �?
							float[] coords = new float[] { event.getX(), event.getY() };
							adjustCoords(coords, -m_fAngle); // ?????��?? ???�? �????. - by ???�?.
							MotionEvent evt = MotionEvent.obtain(event.getDownTime(),
									event.getEventTime(), event.getAction(), coords[0], coords[1],
									event.getPressure(), event.getSize(), event.getMetaState(),
									event.getXPrecision(), event.getYPrecision(),
									event.getDeviceId(), event.getEdgeFlags());
			
							if(evt != null)
								return super.dispatchTouchEvent(evt);
			            }
			            break;
			        case MotionEvent.ACTION_UP:    // 첫�??�? ???�???��?? ??��????? 경�??
			        case MotionEvent.ACTION_POINTER_UP:  // ???�?�? ???�???��?? ??��????? 경�??
			        	m_nTouchMode = NONE;
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:  

			        	//???�?�? ???�???? ??��??(???�???? 2�?�? ??��????????�? ???문�?? ???�? �???��?? ???�?)
			        	m_nTouchMode = ZOOM;

			            break;
			        case MotionEvent.ACTION_CANCEL:
			        default : 
			            break;
			    }
			}
			return super.dispatchTouchEvent(event);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	//?????? 공�????? ?????��?? Method
	protected void adjustCoords(float[] coords, float deg)
	{
		float x = coords[0];
		float y = coords[1];
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		// convert to radians
		float rad = (float) ((deg * Math.PI) / 180F);
		float s = (float) Math.sin(rad);
		float c = (float) Math.cos(rad);
		// translate point back to origin:
		x -= centerX;
		y -= centerY;
		// apply rotation
		float tmpX = x * c - y * s;
		float tmpY = x * s + y * c;
		x = tmpX;
		y = tmpY;
		// translate point back:
		x += centerX;
		y += centerY;
		coords[0] = x;
		coords[1] = y;
	}
}
