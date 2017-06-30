package widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vb.dev.lib.R;


/**
 * 弹框
 * Created by Vieboo on 2016/4/20.
 */
public class CustomerDialog extends Dialog {

    private Context mContext;

    public CustomerDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.mContext = context;
//        this.getWindow().setBackgroundDrawable(
//                new ColorDrawable(Color.RED));
//        this.getWindow().setLayout(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void show() {
        if(!this.isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if(this.isShowing()) {
            super.dismiss();
        }
    }


    /**
     * 获取一个含有标题和内容的提示弹框（内容可为空）
     */
    public void makeSingleAlertDialog(CharSequence title, CharSequence content,
                                CharSequence left,
                                final OnCustomerDialogButtonClickListener listener) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.layout_customer_single_dialog, null, false);
        this.setContentView(contentView);

        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        Button btn_left = (Button) contentView.findViewById(R.id.btn_left);

        if(!TextUtils.isEmpty(content)) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }else {
            tv_content.setVisibility(View.GONE);
        }
        tv_title.setText(title);
        btn_left.setText(left);

        if(null != listener) {
            btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick(null);
                }
            });
        }
    }

    /**
     * 获取一个含有标题和内容的提示弹框（内容可为空）
     */
    public void makeAlertDialog(CharSequence title, CharSequence content,
                                CharSequence left, CharSequence right,
                                final OnCustomerDialogButtonClickListener listener) {
       View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.layout_customer_dialog, null, false);
        this.setContentView(contentView);

        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        Button btn_left = (Button) contentView.findViewById(R.id.btn_left);
        Button btn_right = (Button) contentView.findViewById(R.id.btn_right);

        if(!TextUtils.isEmpty(content)) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }else {
            tv_content.setVisibility(View.GONE);
        }
        tv_title.setText(title);
        btn_left.setText(left);
        btn_right.setText(right);

        if(null != listener) {
            btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick(null);
                }
            });
            btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick(null);
                }
            });
        }
    }

    public void changeAlertTheme(boolean isNight) {

    }

    /**
     * 风火轮
     */
    public void makeLoadingDialog() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.layout_loading_dialog, null, false);
        this.setContentView(contentView);
    }


    public interface OnCustomerDialogButtonClickListener {
        void onLeftClick(Object o);
        void onRightClick(Object o);
    }

}
