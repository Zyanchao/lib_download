package com.example.ui;

import java.util.ArrayList;
import java.util.List;


import download.imageLoader.view.GifMovieView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ImageloaderActivity extends Activity implements OnScrollListener {
    private static final String TAG = "MainActivity";

    private List<String> mUrList = new ArrayList<String>();
    private GridView mImageGridView;
    private BaseAdapter mImageAdapter;

    private boolean mIsGridViewIdle = true;
    private int mImageWidth = 0;
    private boolean mIsWifi = false;
    private boolean mCanGetBitmapFromNetWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);
        initData();
        initView();
    }

    private void initData() {
        String[] imageUrls = {
                "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
    			"http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
    			"http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
    			"http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
        		"http://img.blog.csdn.net/20160114230048304",//gif图
                "http://img.blog.csdn.net/20160114230048304",//gif图
                "http://img.blog.csdn.net/20160114230048304",//gif图
    			"assets://anim.gif",
                "drawable://"+R.drawable.anim,
                	"drawable://"+R.drawable.ic_launcher,
                	"file:///mnt/sdcard/paint.png",
        			"http://img.my.csdn.net/uploads/201407/26/1406383059_2237.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406383058_4330.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406383038_3602.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382942_3079.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382942_8125.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382942_4881.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382941_4559.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382941_3845.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382924_8955.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382923_2141.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382923_8437.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382922_6166.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382922_4843.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382905_5804.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382904_3362.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382904_2312.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382904_4960.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382900_2418.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382881_4490.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382881_5935.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382880_3865.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382880_4662.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382879_2553.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382862_5375.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382862_1748.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382861_7618.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382861_8606.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382861_8949.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382841_9821.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382840_6603.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382840_2405.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382840_6354.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382839_5779.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382810_7578.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382810_2436.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382809_3883.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382809_6269.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382808_4179.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382790_8326.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382789_7174.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382789_5170.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382789_4118.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382788_9532.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382767_3184.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382767_4772.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382766_4924.jpg",
        			"http://img.my.csdn.net/uploads/201407/26/1406382766_5762.jpg",
        };
        for (String url : imageUrls) {
            mUrList.add(url);
        }
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;
        mIsWifi = MyUtils.isWifi(this);
        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        mImageGridView = (GridView) findViewById(R.id.gridView1);
        mImageAdapter = new ImageAdapter(this);
        mImageGridView.setAdapter(mImageAdapter);
        mImageGridView.setOnScrollListener(this);
        mImageGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});

        if (!mIsWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("初次使用会从网络下载大概5MB的图片，确认要下载吗？");
            builder.setTitle("注意");
            builder.setPositiveButton("是", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanGetBitmapFromNetWork = true;
                    mImageAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;

        private ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mUrList.size();
        }

        @Override
        public String getItem(int position) {
            return mUrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item,parent, false);
                holder = new ViewHolder();
                holder.imageView = (GifMovieView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GifMovieView imageView = holder.imageView;
            final String uri = getItem(position);
//            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
//                imageView.setTag(uri);
//                BmLoader.load(uri, imageView, new BackListenerAdapter() {
//                    @Override
//                    public void onSuccess(BitmapDrawable bitmap, Movie movie) {
//                        super.onSuccess(bitmap, movie);
//                    }
//                });


            if (position == 0){
            imageView.setCircle().bind(uri);

            }else if (position == 1){
                imageView.setRectangle().bind(uri);

            }else {
                imageView.setRound(50).bind(uri);

            }
//            imageView.setCircle().bind(uri);
//            imageView.setRectangle().bind(uri);
//            }
            convertView.getLayoutParams().width = mImageWidth;
            convertView.getLayoutParams().height = mImageWidth;
            return convertView;
        }

    }

    private static class ViewHolder {
        public GifMovieView imageView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != OnScrollListener.SCROLL_STATE_FLING) {
            mIsGridViewIdle = true;
//            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // ignored
        
    }
    
}
