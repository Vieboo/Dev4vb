package base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;
    protected List<Fragment> fragmentList;
    protected List<String> titleList;

    public BaseFragmentPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> fragmentList) {
        super(fm);
        this.mContext = mContext;
        this.fragmentList = fragmentList;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.mContext = mContext;
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(null != titleList) {
            return titleList.get(position);
        }
        return "";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
