package com.vb.imageloader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.vb.dev.lib.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Administrator on 2017/2/13 0013.
 */

/**
 *
 * 有来源于Content provider,drawable,assets中，使用的时候也很简单，
         我们只需要给每个图片来源的地方加上Scheme包裹起来(Content provider除外)，
         然后当做图片的url传递到imageLoader中，Universal-Image-Loader框架会根据不同的Scheme获取到输入流

             图片来源于Content provider
             String contentprividerUrl = "content://media/external/audio/albumart/13";
             图片来源于assets
             String assetsUrl = Scheme.ASSETS.wrap("image.png");
             图片来源于
             String drawableUrl = Scheme.DRAWABLE.wrap("R.drawable.image");
 *
 *
 * GirdView,ListView加载图片
         相信大部分人都是使用GridView，ListView来显示大量的图片，
        而当我们快速滑动GridView，ListView，我们希望能停止图片的加载，而在GridView，
        ListView停止滑动的时候加载当前界面的图片，这个框架当然也提供这个功能，使用起来也很简单，
        它提供了PauseOnScrollListener这个类来控制ListView,GridView滑动过程中停止去加载图片，该类使用的是代理模式

             listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
             gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));

        第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，
        如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载
 *
 */

public class ImageLoaderHelper {

    private static Context mContext;
    private static String cache_path;

    /**
     * 图片加载线程策略
     *
     * @author yangzhi
     *
     */
    private static class SingleHolder {
        private static final int CORE_POOL_SIZE = 3;
        private static final int MAXIMUM_POOL_SIZE = 3;
        private static final int KEEP_ALIVE = 1;

        private static final ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                Thread tread = new Thread(r, "ImageThread #"
                        + mCount.getAndIncrement());
                tread.setPriority(Thread.NORM_PRIORITY - 1);
                return tread;
            }
        };

        private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
                35);
        /**
         * An {@link Executor} that can be used to execute tasks in parallel.
         */
        private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    private static final String getCahePath(Context ctx, String cachePath) throws IOException {
        if (cache_path == null) {
            cache_path = Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState()) ? getExternalCacheDir(ctx,
                    cachePath).getPath() : ctx.getCacheDir().getPath();

        }
        if (cachePath == null)
            throw new IOException("FileDir is not existsed!");
        else
            return cache_path;
    }

    public static final String getCahePath(Context ctx) {
        if (cache_path != null)
            return cache_path;
        else
            throw new NullPointerException("You not init,please to init!");
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context, String cachePath) {
        final String cacheDir = cachePath;
        // "hupu/games/cache";
        File ff = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + cacheDir);
        if (!ff.exists())
            ff.mkdirs();
        return ff;
    }

    /**
     * 初始化
     *
     * @param context
     * @param cachePath
     *            传递一个外部缓存保存路径 例如:"xxx/cache"
     * @throws IOException   创建获取缓存路径异常
     */
    public static void init(Context context, String cachePath) throws IOException {
        mContext = context;

        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.color.image_url_empty)
                .showImageOnFail(R.color.image_url_fail).showImageOnLoading(R.color.image_url_loading).cacheInMemory(true)
                .cacheOnDisk(true).build();

        File cacheDir = new File(getCahePath(context, cachePath));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(500)
                .diskCache(
                        new UnlimitedDiskCache(cacheDir, null,
                                new Md5FileNameGenerator()))
                .threadPoolSize(3)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .taskExecutor(SingleHolder.THREAD_POOL_EXECUTOR)
                .taskExecutorForCachedImages(SingleHolder.THREAD_POOL_EXECUTOR)
                .memoryCacheSize(getHeapSize(context) / 8)
                // .memoryCache(new WeakMemoryCache())
                .diskCacheExtraOptions(1080, 1920, null)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static int getHeapSize(final Context context) {
        return ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024;
    }

    /**
     * 没有默认图的加载图片显示
     *
     * @param imageView
     * @param url
     */
    public static void setUrlDrawable(final ImageView imageView,
                                      final String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                .displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    /**
     * 有默认图的加载图片显示
     *
     * @param imageView
     * @param url
     * @param defaultResource
     */
    public static void setUrlDrawable(final ImageView imageView,
                                      final String url, final int defaultResource) {
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResource)
                .showImageForEmptyUri(defaultResource)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultResource)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                //	.displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();

        ImageLoader.getInstance().displayImage(url, imageView, options);

        /**
         * 省流量模式点击加载网络图片
         */
//        if(isLocalFile(url)) {
//            ImageLoader.getInstance().displayImage(url, imageView, options);
//        } else {
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ImageLoader.getInstance().displayImage(url, imageView, options);
//                }
//            });
//        }

    }

    /**
     * 有默认图的加载图片显示
     *
     * @param imageView
     * @param url
     * @param defaultResource
     */
    public static void setUrlDrawable(final ImageView imageView,
                                      final String url, final int defaultResource,boolean power) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResource)
                .showImageForEmptyUri(defaultResource)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultResource)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(power? ImageScaleType.EXACTLY_STRETCHED: ImageScaleType.IN_SAMPLE_POWER_OF_2)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                //	.displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
    /**
     * 有监听的加载图片显示
     *
     * @param imageView
     * @param url
     * @param callback
     */
    public static void setUrlDrawable(final ImageView imageView,
                                      final String url, final ImageLoaderCallback callback) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                .displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();
        ImageLoader.getInstance().displayImage(url, imageView, options,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // TODO Auto-generated method stub
                        callback.onLoadFailue(imageView, null, imageUri);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        callback.onLoadSuccess(imageView, loadedImage, imageUri);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        // TODO Auto-generated method stub
                    }
                }, new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri, View view,
                                                 int current, int total) {
                        // TODO Auto-generated method stub
                        callback.onLoadProgress(current, total);
                    }
                });
    }


    /**
     * 有监听的加载图片显示并有默认图
     *
     * @param imageView
     * @param url
     * @param callback
     */
    public static void setUrlDrawable(final ImageView imageView,
                                      final String url, final int defaultResource, final ImageLoaderCallback callback) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResource)
                .showImageForEmptyUri(defaultResource)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultResource)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                .displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();
        ImageLoader.getInstance().displayImage(url, imageView, options,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // TODO Auto-generated method stub
                        callback.onLoadFailue(imageView, null, imageUri);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        callback.onLoadSuccess(imageView, loadedImage, imageUri);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        // TODO Auto-generated method stub
                    }
                }, new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri, View view,
                                                 int current, int total) {
                        // TODO Auto-generated method stub
                        callback.onLoadProgress(current, total);
                    }
                });
    }

    /**
     * 加载图片不显示图片有回调（异步线程）
     * @param context
     * @param url
     * @param callback
     */
    public static void loadUrlDrawable(final Context context, final String url,final ImageLoaderCallback callback){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码类型//
                .resetViewBeforeLoading(true)
                // 设置图片在下载前是否重置，复位
                .postProcessor(null)
                .displayer(new FadeInBitmapDisplayer(800, true, true, false))
                .resetViewBeforeLoading(true).build();

        ImageLoader.getInstance().loadImage(url, null, options,new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // TODO Auto-generated method stub
                callback.onLoadFailue(null, null, imageUri);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                // TODO Auto-generated method stub
                callback.onLoadSuccess(null, loadedImage, imageUri);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub
            }
        }, new ImageLoadingProgressListener() {

            @Override
            public void onProgressUpdate(String imageUri, View view,
                                         int current, int total) {
                // TODO Auto-generated method stub
                callback.onLoadProgress(current, total);
            }
        });
    }

    /**
     * 获取缓存图片(同步线程)
     *
     * @param url
     * @return
     */
    public static Bitmap getCachedBitmap(String url) {
        return ImageLoader.getInstance().loadImageSync(url);
    }

    public static boolean isLocalFile(String url) {
        File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file != null)
            return file.exists();
        return false;
    }

    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }


}
