package com.zhaohu.niubility.activities;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.views.ZoomImageView;

/**
 * 查看大图的Activity界面。
 *
 * @author guolin
 */
public class ImageDetailsActivity extends Activity implements
		OnPageChangeListener {

	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText;

    private String[] imageUrls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);


		int imagePosition = getIntent().getIntExtra("INDEX", 0);
        imageUrls = getIntent().getStringArrayExtra("IMAGE_ARRAY");

		pageText = (TextView) findViewById(R.id.page_text);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(imagePosition);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
		// 设定当前的页数和总页数
		pageText.setText((imagePosition + 1) + "/" + imageUrls.length);
	}

	/**
	 * ViewPager的适配器
	 *
	 * @author guolin
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(
					R.layout.zoom_image_layout, null);
			ZoomImageView zoomImageView = (ZoomImageView) view
					.findViewById(R.id.zoom_image_view);

            ZhaoHuClient client = ZhaoHuClient.getInstance(ImageDetailsActivity.this);
            client.loadImage(imageUrls[position], zoomImageView);

			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	/**
	 * 获取图片的本地存储路径。
	 *
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {
		// 每当页数发生改变时重新设定一遍当前的页数和总页数
		pageText.setText((currentPage + 1) + "/" + imageUrls.length);
	}

}