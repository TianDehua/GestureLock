package com.tian.gesturelock;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caifangmao on 1/27/16.
 */
public class GestureIndexView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mWidth;
    
    private int mDepth; //圆个数的维度
    private int mGap;	//两圆之间的距离
    private int mCircleRadius; //圆的半径
    private int mGapToRadiusRatio; //圆和间隔的比例
    private ArrayList<Integer> mSeletedArray;

	private int mColorNormal;

	private int mColorSelect;

	private int mStrokeWidth;
    
    

    public GestureIndexView(Context context){
        this(context, null);
    }

    public GestureIndexView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public GestureIndexView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        mDepth = 3;
        mGapToRadiusRatio = 2;
        mColorNormal = context.getResources().getColor(R.color.gray_dark);
        mColorSelect = context.getResources().getColor(R.color.blue_light);
        mSeletedArray = new ArrayList<Integer>();
        mStrokeWidth = 2;
        
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        
        int countInOneLine = mDepth*2 + (mDepth-1)*mGapToRadiusRatio;
        mCircleRadius = (mWidth - mDepth*2)/countInOneLine;
        mGap = mCircleRadius * mGapToRadiusRatio;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	int xPx = 0, yPx = 0;
    	int index = 0;
    	for(int i=0; i<mDepth; i++) {
    		if(i == 0)
    			yPx += mCircleRadius+mStrokeWidth;	//多一个stroke宽度
    		else 
    			yPx += (2*mCircleRadius + mGap);
    		for(int y=0; y<mDepth; y++) {
    			if(y == 0)
    				xPx = mCircleRadius+mStrokeWidth;	//多一个stroke宽度
    			else
    				xPx += (2*mCircleRadius + mGap);
    			if(mSeletedArray.contains(index)) {
    				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    				mPaint.setColor(mColorSelect);
    				mPaint.setStrokeWidth(mStrokeWidth);
    			    canvas.drawCircle(xPx, yPx, mCircleRadius, mPaint);
    			} else {
    				mPaint.setStyle(Paint.Style.STROKE);
    				mPaint.setColor(mColorNormal);
    				mPaint.setStrokeWidth(mStrokeWidth);
    			    canvas.drawCircle(xPx, yPx, mCircleRadius, mPaint);
    			}
    			index ++;
    		}
    	}
    }
    
    public void addSelect(int index) {
    	if(index >=0 && index < mDepth*mDepth) {
	    	mSeletedArray.add(index);
	    	invalidate();
    	}
    }
    
    public void clearSelect() {
    	if(!mSeletedArray.isEmpty()) {
	    	mSeletedArray.clear();
	    	invalidate();
    	}
    }
    
}
