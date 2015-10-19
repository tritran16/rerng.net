package net.rerng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.rerng.adapter.GroupFilmAdatper;
import net.rerng.adapter.ImageSliderAdapter;
import net.rerng.controls.CirclePageIndicator;
import net.rerng.models.Film;
import net.rerng.models.Group;
import net.rerng.services.Films;
import net.rerng.utils.CheckNetworkConnection;
import net.rerng.utils.PageIndicator;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	public static final String ARG_ITEM_ID = "home_fragment";

	private static final long ANIM_VIEWPAGER_DELAY = 5000;
	private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

	// UI References
	private ViewPager mViewPager;
	
	TextView imgNameTxt;
	PageIndicator mIndicator;

	AlertDialog alertDialog;
	
	

	List<Film> films;
	ArrayList<Group> groups;
	GetHotFilmTask task;
	boolean stopSliding = false;
	String message;

	private Runnable animateViewPager;
	private Handler handler;

	//String url = "http://192.168.3.119:8080/films.json";
	FragmentActivity activity;
	
	ListView groupListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		MainActivity main = (MainActivity)getActivity();
        main.setTitle("Home");
        main.setActiveFragment(this, "Home");
		findViewById(view);
		// Add header
        LayoutInflater inflater_header = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater_header.inflate(R.layout.home_slider, groupListView, false);
        groupListView.addHeaderView(header, null, false);
        mViewPager = (ViewPager) header.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
		imgNameTxt = (TextView) header.findViewById(R.id.img_name);
        
		mIndicator.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {

				case MotionEvent.ACTION_CANCEL:
					break;

				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					if (films != null && films.size() != 0) {
						stopSliding = false;
						runnable(films.size());
						handler.postDelayed(animateViewPager,
								ANIM_VIEWPAGER_DELAY_USER_VIEW);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					if (handler != null && stopSliding == false) {
						stopSliding = true;
						handler.removeCallbacks(animateViewPager);
					}
					break;
				}
				return false;
			}
		});

		return view;
	}

	private void findViewById(View view) {
		
		groupListView = (ListView) view.findViewById(R.id.groupListView);
	}

	public void runnable(final int size) {
		handler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (mViewPager.getCurrentItem() == size - 1) {
						mViewPager.setCurrentItem(0);
					} else {
						mViewPager.setCurrentItem(
								mViewPager.getCurrentItem() + 1, true);
					}
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}


	@Override
	public void onResume() {
		if (films == null) {
			sendRequest();
		} else {
			mViewPager.setAdapter(new ImageSliderAdapter(activity, films,
					HomeFragment.this));

			mIndicator.setViewPager(mViewPager);
			imgNameTxt.setText(""
					+ ((Film) films.get(mViewPager.getCurrentItem()))
							.getName());
			runnable(films.size());
			//Re-run callback
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
		}
		super.onResume();
	}

	
	@Override
	public void onPause() {
		if (task != null)
			task.cancel(true);
		if (handler != null) {
			//Remove callback
			handler.removeCallbacks(animateViewPager);
		}
		super.onPause();
	}

	private void sendRequest() {
		if (CheckNetworkConnection.isConnectionAvailable(activity)) {
			task = new GetHotFilmTask();
			task.execute();
			new GetGroupTask().execute();
		} else {
			message = "Cannot connect to Internet. Please try again";//getResources().getString(R.string.no_internet_connection);
			showAlertDialog(message, true);
		}
	}

	public void showAlertDialog(String message, final boolean finish) {
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finish)
							activity.finish();
					}
				});
		alertDialog.show();
	}

	private class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				if (films != null) {
					imgNameTxt.setText(""
							+ ((Film) films.get(mViewPager
									.getCurrentItem())).getName());
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	

	private class GetHotFilmTask extends AsyncTask<String, Void, List<Film>> {
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
		@Override
		protected List<Film> doInBackground(String... params) {
			// TODO Auto-generated method stub
			Films mFilms = new Films();
			films = mFilms.getHotFilms();
			
			return null;
		}
		@Override
        protected void onPostExecute(List<Film> result) {
            super.onPostExecute(result);
            mViewPager.setAdapter(new ImageSliderAdapter(
                    activity, films, HomeFragment.this));

            mIndicator.setViewPager(mViewPager);
            imgNameTxt.setText(""
                    + ((Film) films.get(mViewPager
                            .getCurrentItem())).getName());
            runnable(films.size());
            handler.postDelayed(animateViewPager,
                    ANIM_VIEWPAGER_DELAY);
		}
		
	}
	private class GetGroupTask extends AsyncTask<Void, Void, Integer> {
		 @Override
	        protected Integer doInBackground(Void... params) {
	        	try {
	        		Log.i("GetGroupTask" , "Start get Data");
	        		Films mFilms = new Films();
					groups = mFilms.getGroupFilms();
	        	} catch(Exception ex) {
	        		ex.printStackTrace();
	        	}
	        	return 1;
	        }
	        
	        @Override
	        protected void onPostExecute(Integer result) {
	        	try {
	        		Log.i("GetGroupTask" , "Start onPostExecute");
	        		if(groups != null){
	        			Log.i("GetGroupTask", "groups size: " + groups.size());
		        		GroupFilmAdatper adapter = new GroupFilmAdatper(activity, groups);
		        		groupListView.setAdapter(adapter);
	        		}
	        		else {
	        			Log.i("GetGroupTask", "GROUP IS NULL");
	        		}
		            super.onPostExecute(result);
		            
	        	} catch(Exception ex) {
	        		ex.printStackTrace();
	        		
	        	}
	        }
	}
}