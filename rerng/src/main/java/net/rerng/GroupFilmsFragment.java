package net.rerng;


import java.util.ArrayList;


import net.rerng.adapter.FilmAdatper;
import net.rerng.controls.ExpandableHeightGridView;
import net.rerng.models.Film;
import net.rerng.models.Page;
import net.rerng.services.Films;


import android.R.anim;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class GroupFilmsFragment extends Fragment {
	private String group_id;
	private String type;
	private String title;
	private static final String KEY_CONTENT = "GroupFilmsFragment:Content";
	
	private ExpandableHeightGridView mListView;
	private TextView mNoDataLayout;
	private ProgressBar progressBar;
	
	ArrayList<Film> films;
	
	
	FilmAdatper adapter;
	boolean loadingMore = false;
	int currentPage = 1;
	Page page = new Page();
	
	Films mFilms = new Films();
	ArrayList<Film> moreList = null;
	
	static FragmentActivity activity;
	public GroupFilmsFragment(){
		
	}
	public static GroupFilmsFragment newInstance(String group_id, String type, String title) {
		GroupFilmsFragment fragment = new GroupFilmsFragment();
		fragment.group_id = group_id;
		fragment.type = type;
		fragment.title = title;
		return fragment; 
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		activity = getActivity();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.fragment_group_films, container, false);
        if(group_id != null){
	        MainActivity main = (MainActivity)getActivity();
	        main.setCustomTitle(title);
	        main.setActiveFragment(this, title);
        }
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
		//mLayout = (LinearLayout) view.findViewById(R.id.layout_expert_partners);
		mListView = (ExpandableHeightGridView) view.findViewById(R.id.list);
		mNoDataLayout = (TextView)view.findViewById(R.id.layoutNoItems);
		mListView.setOnScrollListener(new OnScrollListener() {
        	public void onScrollStateChanged(AbsListView view, int scrollState) { 
        		
        	}
        	public void onScroll(AbsListView view, int firstVisibleItem,
        		int visibleItemCount, int totalItemCount) {
        		try {
	        	    if((firstVisibleItem + visibleItemCount == totalItemCount) && !(loadingMore) && totalItemCount < page.GetCount() && currentPage < page.GetPageCount()) {
	        	    	loadingMore = true;	        	    	        	    	
	        	    	Thread thread =  new Thread(null, loadMoreListItems);
	        	    	thread.start();
	        	    	
	        	    }
        		}
        		catch (Exception e) {
					// TODO: handle exception
        			e.printStackTrace();
				}
        		
        	}
    	});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
					
			}
		});

		if (films == null) {
			new GetDataTask().execute();
		} else {
			progressBar.setVisibility(View.GONE);
			adapter = new FilmAdatper(getActivity(), films);
			//mLayout.setVisibility(View.VISIBLE);
			mListView.setAdapter(adapter);
			if(films.size()>0)
				mNoDataLayout.setVisibility(View.GONE);
			else mNoDataLayout.setVisibility(View.VISIBLE);
		}
		
        return view;
    }
	private class GetDataTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
			films = new ArrayList<Film>();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				Log.i("do in backgound" , " start -------------");
				if(type.equals("1"))
				films = mFilms.getFilmsByGenre(group_id, currentPage);
				else films = mFilms.getFilmsByGroup(group_id, currentPage);
			} catch (Exception ex) {
				//Log.e(TAG, "Error on doInBackground-GetPartnersTask", ex);
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			try {
				if (films != null) {
					page = mFilms.GetPage();
					Log.i("Page " , "Count :" + page.GetPageCount());
					if (films.size() > 0) {
						mNoDataLayout.setVisibility(View.GONE);
					} else {
						mNoDataLayout.setVisibility(View.VISIBLE);
					}
					adapter = new FilmAdatper(getActivity(), films);
					mListView.setAdapter(adapter);
				}
				else {
					mNoDataLayout.setVisibility(View.VISIBLE);
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			progressBar.setVisibility(View.GONE);
		}
	}
	
	// Runnable to load the items
		private Runnable loadMoreListItems = new Runnable() {
			public void run() {
				Log.i("loadMoreListItems", "start -------------");
				loadingMore = true;
				try {
				
					currentPage++;
					 if(type.equals("1"))
						 moreList = mFilms.getFilmsByGenre(group_id, currentPage);
					else moreList = mFilms.getFilmsByGroup(group_id, currentPage);
					getActivity().runOnUiThread(returnRes);
				} catch (Exception ex) {
					Log.e("Load more items", ex.getMessage());
				}
			}
		};

		private Runnable returnRes = new Runnable() {
			public void run() {
				if(moreList !=null && moreList.size() > 0){
					films.addAll(moreList);
				}
			   adapter.notifyDataSetChanged();
			   loadingMore = false;
			}
		};
}
