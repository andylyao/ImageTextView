package com.dodola.animview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Yaoll
 * Time on 2015/10/23.
 * Description text里插入图片实现
 */
public class ImageTextView extends TextView {
	public ImageTextView(Context context) {
		super(context);
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setImageText(CharSequence text, String... imageUrls) {
		StringBuffer stringBuffer = new StringBuffer();
		if(imageUrls != null) {
			for(String imageUrl : imageUrls) {
				stringBuffer.append("<img src='");
				stringBuffer.append(imageUrl);
				stringBuffer.append("'/>");
			}
		}
		stringBuffer.append(text);
		new ImageAsyncTask().execute(stringBuffer.toString());
	}

	private class ImageAsyncTask extends AsyncTask<String, Void, CharSequence> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected CharSequence doInBackground(String... params) {
			Html.ImageGetter imageGetter = new Html.ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					Bitmap bitmap = getImageLoader().loadImageSync(source);
					Drawable drawable = new BitmapDrawable(getResources(), bitmap);
					int width = drawable.getMinimumWidth();
					int height = drawable.getMinimumHeight();
					int outHeight = (int) (getPaint().descent() - getPaint().ascent());
					int outWidth = (width * outHeight) / height;
					drawable.setBounds(0, 0, outWidth, outHeight);
					drawable.setLevel(1);
					return drawable;
				}
			};
			CharSequence charSequence = Html.fromHtml(params[0].toString(), imageGetter, null);
			return charSequence;
		}

		@Override
		protected void onPostExecute(CharSequence charSequence) {
			super.onPostExecute(charSequence);
			ImageTextView.super.setText(charSequence);
		}
	}

	private ImageLoader mImageLoader;
	private ImageLoader getImageLoader() {
		if(mImageLoader == null) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())//
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.memoryCache(new WeakMemoryCache())//
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())//
					.tasksProcessingOrder(QueueProcessingType.LIFO)//
					.build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(config);
		}
		return mImageLoader;
	}
}
