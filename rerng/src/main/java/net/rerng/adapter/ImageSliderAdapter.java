package net.rerng.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.rerng.FilmActivity;
import net.rerng.HomeFragment;
import net.rerng.R;
import net.rerng.SearchFilmResultActivity;
import net.rerng.models.Film;
import net.rerng.utils.Constant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageSliderAdapter extends PagerAdapter {
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;
	FragmentActivity activity;
	List<Film> films;
	HomeFragment homeFragment;

	public ImageSliderAdapter(FragmentActivity activity, List<Film> films,
			HomeFragment homeFragment) {
		this.activity = activity;
		this.homeFragment = homeFragment;
		this.films = films;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity.getApplicationContext()));
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.no_image)
				.showStubImage(R.drawable.no_image)
				.showImageForEmptyUri(R.drawable.no_image).cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED) 
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();
	}

	@Override
	public int getCount() {
		return films.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.vp_image, container, false);

		ImageView mImageView = (ImageView) view
				.findViewById(R.id.image_display);
		final Film film = (Film)films.get(position);
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view_film(film);
			}
		});
		Log.i("ImageSliderAdapter", " Start display image");
		
		imageLoader.displayImage(
				Constant.URL + "/images/upload/films/big/" + ((Film) films.get(position)).getLargeImage(), mImageView,
				options, imageListener);
		container.addView(view);
		return view;
	}
	
	private void view_film(Film film){
		Intent i = new Intent(activity , FilmActivity.class);
    	
		i.putExtra("film_id", film.getId());
		i.putExtra("film_name",  film.getName());
		activity.startActivity(i);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private static class ImageDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}