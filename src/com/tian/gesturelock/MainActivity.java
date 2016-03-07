package com.tian.gesturelock;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tian.gesturelock.GestureLock.GestureLockAdapter;
import com.tian.gesturelock.widget.MyStyleLockView2;

public class MainActivity extends Activity {

	private static final int FIRST_STATE = 1;
	private static final int SECOND_STATE = 2;
	private static final int ERROR_STATE = 3;
	private static final int SUCC_STATE = 4;

	private GestureLock gestureView;
	private int mState;
	private ArrayList<Integer> mArrayGestureData;
	private TextView tvGesture;
	private GestureIndexView gestureIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
	}

	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setTitle("手势密码");
		gestureView = (GestureLock) findViewById(R.id.gesture_lock);
		gestureIndex = (GestureIndexView) findViewById(R.id.gesture_index);
		tvGesture = (TextView) findViewById(R.id.tv_gesture_desc);
		
	}
	
	private void initData() {
		mArrayGestureData = new ArrayList<Integer>();
		setGestureState(FIRST_STATE);
		gestureView.setAdapter(updateGestureAdapter());
		gestureView.setOnGestureEventListener(new GestureLock.OnGestureEventListener() {

			@Override
			public void onGestureEvent(boolean matched) {
				if (mState == FIRST_STATE || mState == ERROR_STATE) {
					setGestureState(SECOND_STATE);
				} else if (mState == SECOND_STATE) {
					int state = (matched ? SUCC_STATE : ERROR_STATE);

					setGestureState(state);
				}
			}

			@Override
			public void onUnmatchedExceedBoundary() {
				Toast.makeText(MainActivity.this, "输入5次错误!30秒后才能输入", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onBlockSelected(int position) {
				if (mState == FIRST_STATE || mState == ERROR_STATE) {
					gestureIndex.addSelect(position);
					mArrayGestureData.add(position);
				}
			}
		});
	}

	private void setGestureState(int state) {
		if (state == mState)
			return;
		mState = state;
		switch (state) {
		case FIRST_STATE:
			tvGesture.setText(R.string.gesture_request_paint);
			mArrayGestureData.clear();
			gestureIndex.clearSelect();
			break;
		case SECOND_STATE:
			tvGesture.setText(R.string.gesture_request_again);
			gestureView.clear();
			gestureView.notifyDataChanged();
			break;
		case ERROR_STATE:
			tvGesture.setText(R.string.gesture_edit_again_error);
			mArrayGestureData.clear();
			gestureIndex.clearSelect();
			break;
		case SUCC_STATE:
			tvGesture.setText(R.string.gesture_edit_ok);
			Toast.makeText(MainActivity.this, "配置成功，3S后自动清除状态", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mState = FIRST_STATE;
					gestureView.clear();
					gestureIndex.clearSelect();
				}
			}, 3000);
			break;
		}
		Log.d("state", state + "");
	}

	private GestureLockAdapter updateGestureAdapter() {
		return new GestureLock.GestureLockAdapter() {
			@Override
			public int getDepth() {
				return 3;
			}

			@Override
			public int[] getCorrectGestures() {

				if (mState == SECOND_STATE) {
					int[] firstArrayData = new int[mArrayGestureData.size()];
					for (int i = 0; i < mArrayGestureData.size(); i++) {
						firstArrayData[i] = mArrayGestureData.get(i);
					}
					return firstArrayData;
				}
				return new int[] { 0, 3, 4, 5, 8 };
			}

			@Override
			public int getUnmatchedBoundary() {
				return Integer.MAX_VALUE;
			}

			@Override
			public int getBlockGapSize() {
				return 20;
			}

			@Override
			public GestureLockView getGestureLockViewInstance(Context context, int position) {
				return new MyStyleLockView2(context);
			}
		};
	}



}
