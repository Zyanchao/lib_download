package download.imageLoader.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import download.imageLoader.core.BmLoader;


public class GifMovieView extends ImageView {
	private final int DEFAULT_MOVIEW_DURATION = 1000;
	private Movie mMovie;
	private long mMovieStart;
	private int mCurrentAnimationTime = 0;
	private Paint mPaint;
	private final int TYPE_RECTANGLE = 0,TYPE_CYCLE = 1,TYPE_ROUND = 2;
	private int type = TYPE_RECTANGLE;
	private int boder_radius = 10;

	float scaleH = 1f;
	float scaleW = 1f;

	private float mLeft;
	private float mTop;

	private Path mPath;

	private long nowTime,dur;
	private float mScale;

	private int mMeasuredMovieWidth;
	private int mMeasuredMovieHeight;

	private boolean mVisible = true;

	public GifMovieView(Context context) {
		this(context, null);
	}

	public GifMovieView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GifMovieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPath = new Path();
		setBackgroundColor(Color.TRANSPARENT);
	}


	public GifMovieView setCircle(){
		this.type = TYPE_CYCLE;
		requestLayout();
		return this;
	}
	public GifMovieView setRectangle(){
		this.type = TYPE_RECTANGLE;
		requestLayout();
		return this;
	}
	public GifMovieView setRound(int round){
		this.type = TYPE_ROUND;
		if (round > 0){
			boder_radius = round;
		}
		requestLayout();
		return this;
	}
	public void bind(String path){
		BmLoader.load(path, this);
	}

	public void setMovie(Movie movie) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("only run on ui thread");
		}
		this.mMovie = movie;
		//至关重要的设置
		setImageDrawable(null);
		notifyLayerType();
		requestLayout();
	}

	private void notifyLayerType() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mMovie != null && getDrawable() == null){
				setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}else {
				setLayerType(View.LAYER_TYPE_NONE, null);
			}
		}
	}


	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.clearAnimation();
		this.mMovie = null;
		this.setImageDrawable(null);
	}

	public void setMovieTime(int time) {
		mCurrentAnimationTime = time;
		invalidate();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
		float maximumWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		float maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (mMovie != null && getDrawable() == null){
			float movieWidth = mMovie.width();
			float movieHeight = mMovie.height();
			if (measureModeWidth == MeasureSpec.AT_MOST && measureModeHeight == MeasureSpec.AT_MOST){
				if (movieHeight > maximumHeight && maximumHeight > 0)
					scaleH = movieHeight/maximumHeight;
				if (movieWidth > maximumWidth && maximumWidth > 0)
					scaleW = movieWidth/maximumWidth;
			}else {
				if (maximumHeight > 0)
					scaleH = movieHeight/maximumHeight;
				if (maximumWidth > 0)
					scaleW = movieWidth/maximumWidth;
			}

			if (Math.max(scaleH,scaleW) > 0){
				mScale = 1f/Math.max(scaleH,scaleW);
			}else {
				mScale = 1.0f;
			}
			mMeasuredMovieWidth = (int) (movieWidth * mScale);
			mMeasuredMovieHeight = (int) (movieHeight * mScale);
			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
		} else {
			super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		}
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		notifyLayerType();
		invalidateView();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		notifyLayerType();
		invalidateView();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		notifyLayerType();
		invalidateView();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		notifyLayerType();
		invalidateView();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mLeft = (getWidth() - mMeasuredMovieWidth + getPaddingLeft() + getPaddingRight()) / 2f;
		mTop = (getHeight() - mMeasuredMovieHeight + getPaddingTop() + getPaddingBottom()) / 2f;
		mVisible = getVisibility() == View.VISIBLE;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onDraw(Canvas canvas) {
		if (!mVisible){
			Log.v("test","mVisible:"+mVisible);
			return;
		}
		if (mMovie != null && getDrawable() == null) {
			drawMovieFrame(canvas);
			invalidateView();
		}else {
			//直接修改ImageView源代码
//			super.onDraw(canvas);
			if (getDrawable() == null) {
				return; // couldn't resolve the URI
			}

			if (getDrawable().getIntrinsicWidth() <= 0 || getDrawable().getIntrinsicHeight() <= 0) {
				return;     // nothing to draw (empty bounds)
			}

			if (getImageMatrix() == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
				clipDrawable(canvas);
				getDrawable().draw(canvas);
			} else {
				int saveCount = canvas.getSaveCount();
				canvas.save();

				if (getCropToPadding()) {
					final int scrollX = getScrollX();
					final int scrollY = getScrollY();
					canvas.clipRect(scrollX + getPaddingLeft(), scrollY + getPaddingTop(),
							scrollX + getRight() - mLeft - getPaddingRight(),
							scrollY + getBottom() - mTop - getPaddingBottom());
				}

				clipDrawable(canvas);

				canvas.translate(getPaddingLeft(), getPaddingTop());

				if (getImageMatrix() != null) {
					canvas.concat(getImageMatrix());
				}

				getDrawable().draw(canvas);
				canvas.restoreToCount(saveCount);
			}
		}
	}
	private void clipDrawable(Canvas canvas){
		mPath.reset();
		switch (type){
			case TYPE_CYCLE:
				canvas.clipPath(mPath); // makes the clip empty
				mPath.addCircle(getWidth() / 2 , getHeight() / 2 ,
						Math.min(getWidth(), getHeight()) / 2 , Path.Direction.CCW);
				canvas.clipPath(mPath, Region.Op.REPLACE);
				break;
			case TYPE_ROUND://纠结是该让gif圆角化还是view圆角化，貌似让gif圆角好看点。但是按理是该让view圆角。当填充方式是centerscrop时两者效果一样
				canvas.clipPath(mPath); // makes the clip empty
				RectF rect = new RectF(0,0, (int) (getWidth()), (int) (getHeight()));
				mPath.addRoundRect(rect, new float[]{boder_radius, boder_radius, boder_radius, boder_radius,boder_radius, boder_radius, boder_radius, boder_radius}, Path.Direction.CCW);
				canvas.clipPath(mPath, Region.Op.REPLACE);
				break;
			case TYPE_RECTANGLE:

				break;
		}
	}


	private void invalidateView() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
	}

	private void drawMovieFrame(Canvas canvas) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		clipGif(canvas);
		updateTime();
		mMovie.setTime(mCurrentAnimationTime);
		mMovie.draw(canvas, mLeft / mScale, mTop / mScale, mPaint);
		canvas.restore();
	}

	private void clipGif(Canvas canvas){
		mPath.reset();
		switch (type){
			case TYPE_CYCLE:
				canvas.clipPath(mPath); // makes the clip empty
				mPath.addCircle(getWidth() / 2 / mScale, getHeight() / 2 /mScale,
						Math.min(getWidth(), getHeight()) / 2 / mScale * Math.max(scaleH,scaleW) / Math.min(scaleH,scaleW), Path.Direction.CCW);
				canvas.clipPath(mPath, Region.Op.REPLACE);
				break;
			case TYPE_ROUND://纠结是该让gif圆角化还是view圆角化，貌似让gif圆角好看点。但是按理是该让view圆角。当填充方式是centerscrop时两者效果一样
				canvas.clipPath(mPath); // makes the clip empty
				RectF rect = new RectF(0,0, (int) (getWidth()/mScale), (int) (getHeight()/mScale));
				mPath.addRoundRect(rect, new float[]{boder_radius/mScale, boder_radius/mScale, boder_radius/mScale, boder_radius/mScale,boder_radius/mScale, boder_radius/mScale, boder_radius/mScale, boder_radius/mScale}, Path.Direction.CCW);
				canvas.clipPath(mPath, Region.Op.REPLACE);
				break;
			case TYPE_RECTANGLE:

				break;
		}
	}

	private void updateTime(){
		nowTime = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) {
			mMovieStart = nowTime;
		}
		dur = mMovie.duration();
		if (dur == 0) {
			dur = DEFAULT_MOVIEW_DURATION;
		}
		mCurrentAnimationTime = (int) ((nowTime - mMovieStart) % dur);
	}

	@SuppressLint("NewApi")
	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		mVisible = screenState == SCREEN_STATE_ON;
		invalidateView();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}
}
