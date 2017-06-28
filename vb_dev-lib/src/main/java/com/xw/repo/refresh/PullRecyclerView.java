package com.xw.repo.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class PullRecyclerView extends RecyclerView implements Pullable {

    private boolean pullDownEnable = true; //下拉刷新开关
    private boolean pullUpEnable = true; //上拉刷新开关

    public PullRecyclerView(Context context) {
        super(context);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (!pullDownEnable) {
            return false;
        }
        int first = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        if (getAdapter().getItemCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (first == 0 && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        if (!pullUpEnable) {
            return false;
        }
        int first = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        int last = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
        if (getAdapter().getItemCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (last == (getAdapter().getItemCount() - 1)) {
            // 滑到底部了
            if (getChildAt(last - first) != null
                    && getChildAt(last - first).getBottom() <= getMeasuredHeight()) {
                return true;
            }
        }
        return false;
    }


    public boolean isPullDownEnable() {
        return pullDownEnable;
    }

    public void setPullDownEnable(boolean pullDownEnable) {
        this.pullDownEnable = pullDownEnable;
    }

    public boolean isPullUpEnable() {
        return pullUpEnable;
    }

    public void setPullUpEnable(boolean pullUpEnable) {
        this.pullUpEnable = pullUpEnable;
    }

}
