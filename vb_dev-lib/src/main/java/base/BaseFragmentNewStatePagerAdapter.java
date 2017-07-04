package base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Vieboo on 2016/4/7.
 */
public abstract class BaseFragmentNewStatePagerAdapter<T> extends FragmentStatePagerAdapter {

    protected List<T> mDatas;
    private Map<Integer, Fragment> cacheMap;

    public Fragment getFragment(int postion) {
        return cacheMap.get(new Integer(postion));
    }

    public BaseFragmentNewStatePagerAdapter(FragmentManager fm, List<T> datas) {
        super(fm);
        this.mDatas = datas;
        cacheMap = new HashMap<>();
    }

    protected abstract CharSequence pageTitle(int position);
    protected abstract Fragment getFragmentInstance (int position);

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle(position);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = getFragmentInstance(position);
        cacheMap.remove(new Integer(position));
        cacheMap.put(new Integer(position), fragment);
        return fragment;
    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }
}
