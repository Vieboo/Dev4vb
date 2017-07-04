package base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class BaseViewHolder {

    public BaseViewHolder(View v) {
        ButterKnife.bind(this, v);
    }

}
