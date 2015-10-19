package net.rerng.adapter;

import java.util.ArrayList;

import com.androidquery.AQuery;

import net.rerng.FilmActivity;
import net.rerng.FilmFragment;
import net.rerng.GroupFilmsFragment;
import net.rerng.R;
import net.rerng.controls.ImageLoader;
import net.rerng.models.Film;
import net.rerng.utils.Constant;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class FilmAdatper extends ArrayAdapter<Film> {
	
	private  FragmentActivity context;
	private ArrayList<Film> values;
	static class ViewHolder {
		ImageView image;
		TextView name;
		//TextView info;
	}
	
	public FilmAdatper(FragmentActivity context, ArrayList<Film> values) {
		super(context, R.layout.list_film_item, values);
		this.context = context;
		this.values = values;
		
	}
	public FilmAdatper( Activity context, ArrayList<Film> values) {
		super(context, R.layout.list_film_item, values);
		this.values = values;
	}
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ImageView image;
		
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_film_item, null);
			ViewHolder holder = new ViewHolder();
			holder.name = (TextView) rowView.findViewById(R.id.filmNameText);
		    holder.image = (ImageView) rowView.findViewById(R.id.filmImage);
		    //holder.info = (TextView) rowView.findViewById(R.id.filmInfoText);
			rowView.setTag(holder);
		}
		
	    try {
		    final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		    String img = values.get(position).getImage();
		    final int pos = position;
		    if(img == null || img=="")
		    	viewHolder.image.setImageResource(R.drawable.no_image);
		    else 
		    {
//		    	final AQuery list = new AQuery(context);
//			    AQuery aq = list.recycle(convertView);
//			    Bitmap b;
//		
//				b = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image);
//				aq.id(viewHolder.image).image(Constant.URL + "/images/upload/films/normal/" +  values.get(position).getImage(), true, true, 200, R.drawable.no_image, b, 0,  1.0f / 1.0f);
					
		    	ImageLoader imageLoader;	
	        	imageLoader = new ImageLoader(context.getBaseContext());
	        	imageLoader.SetImageWidth(200);
	        	imageLoader.SetImageHeight(200);
	        	imageLoader.SetStubId(R.drawable.no_image);
	        	imageLoader.DisplayImage(Constant.URL + "/images/upload/films/normal/" +  values.get(position).getImage(), viewHolder.image);
		    }
		    Log.i("Film Name", values.get(position).getName());
		    viewHolder.name.setText(values.get(position).getName());
		    
		    viewHolder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startLoadFilm(values.get(pos).getId(), values.get(pos).getName());
				}
			});
		    
	    }
	    catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}
	    return rowView;
	}
	
	private void startLoadFilm(String id, String name){
//		FragmentManager fragmentManager = context.getSupportFragmentManager();
//		 Fragment fragment = FilmFragment.newInstance(id, name);
//        fragmentManager.beginTransaction()
//                .replace(R.id.frame_container, fragment).commit();
		Intent i  = new Intent(getContext(), FilmActivity.class);
		i.putExtra("film_id", id);
		i.putExtra("film_name", name);
		Log.i("Film Name" , name);
		getContext().startActivity(i);
	}
}
