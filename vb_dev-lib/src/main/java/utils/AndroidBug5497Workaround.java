//Workaround to get adjustResize functionality for input methos when the fullscreen mode is on  
//found by Ricardo  
//taken from http://stackoverflow.com/a/19494006  
package utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


/**
 * 全屏模式下，即使将activity的windowSoftInputMode的属性设置为：adjustResize，在键盘显示时它未将Activity的Screen向上推动，
 * 所以你Activity的view的根树的尺寸是没有变化的。在这种情况下，你也就无法得知键盘的尺寸，对根view的作相应的推移。
 * 全屏下的键盘无法Resize的问题从2.1就已经存在了，直到现在google还未给予解决。
 * 感谢Ricardo提供的轮子，他在stackoverflow找到了解决方案。有人已经封装好了该类，你只需引用就OK了。

 * 使用方法
 * 在你的Activity的oncreate()方法里调用AndroidBug5497Workaround.assistActivity(this);即可。注意：在setContentView(R.layout.xxx)之后调用。
 */


public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497  
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.  

    public static void assistActivity (Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(final Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(activity);
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(Activity activity) {
        int usableHeightNow = computeUsableHeight(activity);
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible  
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden  
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight(Activity activity) {
        int statusBarH = BarUtils.getStatusBarHeight(activity);
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        if(r.top==0){
            r.top=statusBarH;//状态栏目的高度
        }
        return (r.bottom - r.top);
    }

}  