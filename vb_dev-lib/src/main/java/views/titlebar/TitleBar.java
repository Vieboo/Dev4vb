package views.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vb.dev.lib.R;

import utils.KeyboardUtils;


/**
 * TitleBar
 * Created by Vieboo on 2016/4/1.
 */
public class TitleBar extends RelativeLayout {

    private FrameLayout title_frame;
    private ImageView title_left_icon, title_right_icon, title_middle_icon;
    private TextView title_left_label, title_right_label, title_middle_label;
    private LinearLayout layout_left, layout_right;

    private RelativeLayout layout_normal;

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_titlebar, this);
        title_frame = (FrameLayout) findViewById(R.id.title_frame);
        title_left_icon = (ImageView) findViewById(R.id.title_left_icon);
        title_right_icon = (ImageView) findViewById(R.id.title_right_icon);
        title_middle_icon = (ImageView) findViewById(R.id.title_middle_icon);
        title_left_label = (TextView) findViewById(R.id.title_left_label);
        title_right_label = (TextView) findViewById(R.id.title_right_label);
        title_middle_label = (TextView) findViewById(R.id.title_middle_label);
        layout_left = (LinearLayout) findViewById(R.id.layout_left);
        layout_right = (LinearLayout) findViewById(R.id.layout_right);

        layout_normal = (RelativeLayout) findViewById(R.id.layout_normal);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInEditMode()) {
            return;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence chars) {
        title_middle_label.setText(chars);
        title_middle_label.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左边的图标和文字
     * @param resourseId
     * @param chars
     */
    public void setLeft(int resourseId, CharSequence chars) {
        setLeftIcon(resourseId);
        setLeftLabel(chars);
    }

    /**
     * 设置左边图标
     * @param resourseId
     */
    public void setLeftIcon(int resourseId) {
        if(0 == resourseId) {
            title_left_icon.setVisibility(View.GONE);
        }else {
            title_left_icon.setImageResource(resourseId);
            title_left_icon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置返回按钮
     */
    public void setBackAble(final Activity activity) {
        title_left_icon.setImageResource(R.drawable.ic_title_back_white);
        title_left_icon.setVisibility(VISIBLE);
        title_left_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.dismissKeyboard(activity);
                activity.finish();
            }
        });
    }

    /**
     * 设置返回按钮
     */
    public void setBackWZAble(final Activity activity) {
        title_left_label.setText("返回");
        title_left_label.setTextColor(Color.WHITE);
        title_left_label.setVisibility(VISIBLE);
        title_left_label.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.dismissKeyboard(activity);
                activity.finish();
            }
        });
    }

    /**
     * 设置左边文字
     * @param chars
     */
    public void setLeftLabel(CharSequence chars) {
        if(TextUtils.isEmpty(chars)) {
            title_left_label.setVisibility(View.GONE);
        }else {
            title_left_label.setText(chars);
            title_left_label.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置右边的图标和文字
     * @param resourseId
     * @param chars
     */
    public void setRight(int resourseId, CharSequence chars) {
        setRightIcon(resourseId);
        setRightLabel(chars);
    }

    /**
     * 设置右边图标
     * @param resourseId
     */
    public void setRightIcon(int resourseId) {
        if(0 == resourseId) {
            title_right_icon.setVisibility(View.GONE);
        }else {
            title_right_icon.setImageResource(resourseId);
            title_right_icon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右边文字
     * @param chars
     */
    public void setRightLabel(CharSequence chars) {
        if(TextUtils.isEmpty(chars)) {
            title_right_label.setVisibility(View.GONE);
        }else {
            title_right_label.setText(chars);
            title_right_label.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置右边文字
     * @param color
     */
    public void setRightLabelColor(int color) {
        title_right_label.setTextColor(color);
    }

    /**
     * 左侧layout点击事件
     * @param leftClick
     */
    public void setLeftOnclickListener(OnClickListener leftClick) {
        layout_left.setOnClickListener(leftClick);
    }

    /**
     * 右侧layout点击事件
     * @param rightClick
     */
    public void setRightOnclickListener(OnClickListener rightClick) {
        layout_right.setOnClickListener(rightClick);
    }

    public View getLayoutRight() {
        return layout_right;
    }

    public FrameLayout getTitle_frame() {
        return title_frame;
    }

    public ImageView getTitle_left_icon() {
        return title_left_icon;
    }

    public ImageView getTitle_right_icon() {
        return title_right_icon;
    }

    public TextView getTitle_left_label() {
        return title_left_label;
    }

    public TextView getTitle_right_label() {
        return title_right_label;
    }

    public ImageView getTitle_middle_icon() {
        return title_middle_icon;
    }

    public TextView getTitle_middle_label() {
        return title_middle_label;
    }

}
