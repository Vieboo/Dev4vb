package vieboo.photoview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vb.dev.lib.R;
import vieboo.imageloader.ImageLoaderHelper;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by Administrator on 2017/4/8 0008.
 */

public class ImagePagerFragment extends Fragment {


    private int imgType = PhotoViewConstant.TYPE_URL;
    private int[] imgReses;
    private String[] imgUrls;
    private int imgPostion = 0;

    private ViewPager image_pager;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        if(null != image_pager) image_pager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != getArguments()) {
            imgType = getArguments().getInt(PhotoViewConstant.INTENT_TYPE, PhotoViewConstant.TYPE_URL);
            imgUrls = getArguments().getStringArray(PhotoViewConstant.INTENT_IMG_URLS);
            imgReses = getArguments().getIntArray(PhotoViewConstant.INTENT_IMG_RESES);
            imgPostion = getArguments().getInt(PhotoViewConstant.INTENT_IMG_POSITION, 0);
        }
        if(null == imgUrls) imgUrls = new String[0];
        if(null == imgReses) imgReses = new int[0];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_imagepager, container, false);
        image_pager = (ViewPager) contentView.findViewById(R.id.image_pager);
        image_pager.setAdapter(new SamplePagerAdapter());
        image_pager.setCurrentItem(imgPostion);
        if(null != onPageChangeListener) image_pager.addOnPageChangeListener(onPageChangeListener);
        return contentView;
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if(imgType == PhotoViewConstant.TYPE_RES) {
                return imgReses.length;
            }else {
                return imgUrls.length;
            }
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            if(imgType == PhotoViewConstant.TYPE_RES) {
                photoView.setImageResource(imgReses[position]);
            }else {
                ImageLoaderHelper.setUrlDrawable(photoView, imgUrls[position]);
            }

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
