package vieboo.imageloader;


import android.graphics.Bitmap;
import android.widget.ImageView;

public interface ImageLoaderCallback {
	public void onLoadSuccess(ImageView imageView, Bitmap loadedBitmap,
							  String url);
	
	public void onLoadFailue(ImageView imageView, Bitmap loadedBitmap,
							 String url);
	
	public void onLoadProgress(long current, long total);
}
