package com.tian.gesturelock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.tian.gesturelock.GestureLockView;
import com.tian.gesturelock.R;

/**
 * Created by caifangmao on 1/27/16.
 */
public class MyStyleLockView2 extends GestureLockView {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCleanPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mCenterX, mCenterY;
    private int mWidth, mHeight;
    private int mRadius;

//    private static final int COLOR_NORMAL = 0xFFFFFFFF;
//    private static final int COLOR_ERROR = 0xFFFF0000;
    
    private static final int STROKE_WIDTH = 4;
    

    private float innerRate = 0.3F; //0.2F; //内圆的比例
    //private float outerWidthRate = 0.10F; //0.15F; //圆环的粗细 设成指定 4个像素
    private float outerRate = 0.80F; //0.91F; //画圆的半径

    private float arrowRate = 0.2F; //0.3F; //箭头的比例
    private float arrowDistanceRate = 0.65F; //0.65F; //箭头的位置
    private int arrowDistance;	//箭头距圆心的距离

    private Path arrow;
    private int colorNormal;
    private int colorSelect;
    private int colorError;
    private int colorClean;

    public MyStyleLockView2(Context context){
        this(context, null);
    }

    public MyStyleLockView2(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MyStyleLockView2(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        arrow = new Path();
        colorNormal = context.getResources().getColor(R.color.gray_dark);
        colorSelect = context.getResources().getColor(R.color.blue_light);
        colorError = context.getResources().getColor(R.color.red_light);
        colorClean = context.getResources().getColor(R.color.gray_white);
        
        mCleanPaint.setStyle(Paint.Style.FILL);
        mCleanPaint.setColor(colorClean);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = w > h ? h : w;
        mRadius /= 2;

        arrowDistance = (int) (mRadius * arrowDistanceRate);

        int length = (int) (mRadius * arrowRate);
        arrow.reset();
        arrow.moveTo(mCenterX - length, mCenterY + length - arrowDistance);
        arrow.lineTo(mCenterX, mCenterY - arrowDistance);
        arrow.lineTo(mCenterX + length, mCenterY + length - arrowDistance);
        arrow.close();
    }

    @Override
    protected void doArrowDraw(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(arrow, mPaint);
    }

    @Override
    protected void doDraw(LockerState state, Canvas canvas){
        switch(state){
            case LOCKER_STATE_NORMAL:
			    mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(colorNormal);
                mPaint.setStrokeWidth(STROKE_WIDTH);
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);
			break;
            case LOCKER_STATE_SELECTED:
            	//添加清除line的绘制
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mCleanPaint);
			    
			    //绘制外圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(colorSelect);
                mPaint.setStrokeWidth(STROKE_WIDTH);
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);
			    
			    //绘制内圆
			    mPaint.setStyle(Paint.Style.FILL);
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);
			break;
            case LOCKER_STATE_ERROR:
            	//添加清除line的绘制
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mCleanPaint);
			    
            	//绘制外圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(colorError);
                mPaint.setStrokeWidth(STROKE_WIDTH);
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);
			    
			    //绘制内圆
			    mPaint.setStyle(Paint.Style.FILL);
			    canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);
			break;
		}
    }
}
