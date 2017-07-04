package base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Vieboo on 2016/4/7.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;

    public BaseListAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        if(null == mDatas) return 0;
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if(null == mDatas) return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, mDatas.get(position));
    }

    protected abstract View getView(int position, View convertView, T data);
}
