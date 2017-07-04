package base;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3 0003.
 */

public class BaseFragmentStatePagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    FragmentManager fragmentManager;
    List<Fragment> fragments;
    List<String> titleList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragmentManager = fm;
        this.fragments = fragments;
        this.titleList = titles;
    }

    public void setFragments(List<Fragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fragmentManager.executePendingTransactions();
            this.fragments.clear();
        }
        this.fragments.addAll(fragments);
        notifyDataSetChanged();
    }

    public void setFragments(List<Fragment> fragments, List<String> titleList) {
        if(null != this.titleList) this.titleList.clear();
        this.titleList.addAll(titleList);
        setFragments(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments.size() > position) {
            return  fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(null != titleList) {
            return titleList.get(position);
        }
        return "";
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
