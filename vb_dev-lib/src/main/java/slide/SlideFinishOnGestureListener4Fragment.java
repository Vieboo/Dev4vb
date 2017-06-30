package slide;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;


public class SlideFinishOnGestureListener4Fragment implements OnGestureListener {

	public interface OnSlideRightFinishListener {
		void onFinish();
	}
	private OnSlideRightFinishListener mOnSlideRightFinishListener = null;

	public OnSlideRightFinishListener getmOnSlideRightFinishListener() {
		return mOnSlideRightFinishListener;
	}

	public void setmOnSlideRightFinishListener(OnSlideRightFinishListener mOnSlideRightFinishListener) {
		this.mOnSlideRightFinishListener = mOnSlideRightFinishListener;
	}

	//mdpi下的比例计算;
	private static final float FACTOR_PORTRAIT;
	private static final float FACTOR_LANDSCAPE;
	//手指在屏幕上移动距离小于此值不会被认为是手势
	private static int SLIDE_MIN_DISTANCE_X;
	private static int SLIDE_MAX_DISTANCE_Y;

	private static int DISPLAY_WINDOW_WIDTH;
	private static int DISPLAY_WINDOW_HEIGHT;
	static {
		FACTOR_PORTRAIT = 100f / 320;
		FACTOR_LANDSCAPE = FACTOR_PORTRAIT;
	}

	private SlideDirection slideDirection;


	public SlideFinishOnGestureListener4Fragment(Context context, SlideDirection slideDirection) {
		this.slideDirection = slideDirection;
		initEnv(context);
	}

	private void initEnv(Context context) {
		// 获得屏幕大小
		WindowManager windowManager = ((Activity)context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DISPLAY_WINDOW_WIDTH = display.getWidth();
		DISPLAY_WINDOW_HEIGHT = display.getHeight();

		SLIDE_MIN_DISTANCE_X = (int)(DISPLAY_WINDOW_WIDTH * FACTOR_PORTRAIT);
		SLIDE_MAX_DISTANCE_Y = (int)(DISPLAY_WINDOW_HEIGHT * FACTOR_LANDSCAPE);
		//orientation = context.getResources().getConfiguration().orientation;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float x1 = (e1 != null ? e1.getX() : 0);
		float x2 = (e2 != null ? e2.getX() : 0);
		float y1 = (e1 != null ? e1.getY() : 0);
		float y2 = (e2 != null ? e2.getY() : 0);
		float distanceX = x1 - x2;
		float distanceY = y1 - y2;


		if (
			distanceX < 0
			&& Math.abs(distanceX) > SLIDE_MIN_DISTANCE_X
			&& Math.abs(distanceY) < SLIDE_MAX_DISTANCE_Y
			&& slideDirection == SlideDirection.RIGHT) {
			if(null != mOnSlideRightFinishListener) mOnSlideRightFinishListener.onFinish();
			return true;
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {


	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public SlideDirection getSlideDirection() {
		return slideDirection;
	}

	public void setSlideDirection(SlideDirection slideDirection) {
		this.slideDirection = slideDirection;
	}

}
