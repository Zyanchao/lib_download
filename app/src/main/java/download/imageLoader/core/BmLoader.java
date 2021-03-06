package download.imageLoader.core;

import android.content.Context;
import android.view.View;
import download.imageLoader.listener.BackListener;
import download.imageLoader.listener.BackListenerAdapter;

/**
 * 为了调用更简单添加此门面
 * @author lizhiyun
 *
 */
public class BmLoader {
	/**
	 * preload
	 * 
	 * @param path
	 */
	public static void preLoad(String path) {
		ImageLoader.getInstance().preLoad(path);
	}
	/**
	 * 
	 * @param path示例："http://img.blog.csdn.net/20160114230048304",//gif图
 	 * "assets//:test.png",
     * "drawable//:"+R.drawable.common_logo,
     * "file:///mnt/sdcard/paint.png",
	 * @param view
	 * @param listener
	 */
	public static void load(final String path, final View view, BackListener listener) {
		ImageLoader.getInstance().loadImage(path, view, listener);
	}

	/**
	 *
	 * @param path示例："http://img.blog.csdn.net/20160114230048304",//gif图
	 * "assets//:test.png",
	 * "drawable//:"+R.drawable.common_logo,
	 * "file:///mnt/sdcard/paint.png",
	 * @param view
	 * @param listener
	 */
	public static void load(final String path, final View view, BackListenerAdapter listener) {
		ImageLoader.getInstance().loadImage(path, view, listener);
	}

	/**
	 * 
	 * @param path示例："http://img.blog.csdn.net/20160114230048304",//gif图
 	 * "assets//:test.png",
     * "drawable//:"+R.drawable.common_logo,
     * "file:///mnt/sdcard/paint.png",
	 * @param view
	 */
	public static void load(final String path, final View view) {
		ImageLoader.getInstance().loadImage(path, view);
	}

	/**
	 * set longding and load failed bitmap resource.
	 * @param context
	 * @param loadingId
	 * @param failedId
	 */
	public static void setLoadingAndFailedId(Context context, int loadingId,
											 int failedId) {
		ImageLoader.getInstance().setLoadingAndFailedId(context, loadingId, failedId);
	}

	/**
	 * 设置是否仅wifi下下载模式
	 * @param b
	 */
	public static void setOnlyWifiMode(Boolean b){
		ImageLoader.getInstance().getConfig().setOnlyWifiMode(b);
	}

	/**
	 * 设置是否仅使用缓存模式
	 * @param b
	 */
	public static void setOnlyMemoryMode(Boolean b){
		ImageLoader.getInstance().getConfig().setOnlyMemoryMode(b);
	}

}
