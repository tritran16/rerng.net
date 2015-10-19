package net.rerng.adapter;

import java.util.ArrayList;

import net.rerng.GroupFilmsFragment;
import net.rerng.R;
import net.rerng.controls.ExpandableHeightGridView;
import net.rerng.models.Film;
import net.rerng.models.Group;


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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class GroupFilmAdatper extends ArrayAdapter<Group> {
	
	private final FragmentActivity context;
	private ArrayList<Group> values;
	static class ViewHolder {
		
		TextView name;
		
		ExpandableHeightGridView list;
		
		TextView btn_view_all;
	}
	
	public GroupFilmAdatper(FragmentActivity context, ArrayList<Group> values) {
		super(context, R.layout.list_group_item, values);
		this.context = context;
		this.values = values;
		
	}

	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ImageView image;
		
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_group_item, null);
			ViewHolder holder = new ViewHolder();
			holder.name = (TextView) rowView.findViewById(R.id.group_name);
		    holder.list  = (ExpandableHeightGridView) rowView.findViewById(R.id.group_grid_view);
		    
		    holder.btn_view_all = (TextView)rowView.findViewById(R.id.btnViewAll);
			rowView.setTag(holder);
		}
		
	    try {
		    final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		   final int pos = position;
		    viewHolder.name.setText(values.get(position).getName());
		    FilmAdatper adapter = new FilmAdatper(context, values.get(position).getFilms());
		    viewHolder.list.setAdapter(adapter);
		    viewHolder.list.setExpand(true);
		    viewHolder.btn_view_all.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String group_id = values.get(pos).getId();
					String type = values.get(pos).getType();
					loadFragment(group_id,type, values.get(pos).getName());
				}
			});
		    
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
			// TODO: handle exception
		}
	    return rowView;
	}
	private void loadFragment(String group_id, String type, String title) {
		 FragmentManager fragmentManager = context.getSupportFragmentManager();
		 Fragment fragment = GroupFilmsFragment.newInstance(group_id, type, title);
         fragmentManager.beginTransaction()
                 .replace(R.id.frame_container, fragment).commit();

//         // update selected item and title, then close the drawer
//         mDrawerList.setItemChecked(position, true);
//         mDrawerList.setSelection(position);
//         setTitle(navMenuTitles[position]);
//         mDrawerLayout.closeDrawer(mDrawerList);
	}
	

}
