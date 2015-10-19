package net.rerng;

import java.util.ArrayList;

import net.rerng.adapter.FilmAdatper;
import net.rerng.controls.ExpandableHeightGridView;
import net.rerng.models.Film;
import net.rerng.models.Page;
import net.rerng.services.Films;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class SearchFilmResultActivity extends FragmentActivity {

	private ExpandableHeightGridView mListView;
	//private TextView mNoDataLayout;
	private ProgressBar progressBar;
	
	ArrayList<Film> films;
	
	
	FilmAdatper adapter;
	boolean loadingMore = false;
	int currentPage = 1;
	Page page = new Page();
	
	Films mFilms = new Films();
	ArrayList<Film> moreList = null;
	
	String keyword;
	ActionBar actionBar;
	
	//EditText mSearchText;
	TextView mSearhResultText;
	SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_film);
//		mSearchText = (EditText)findViewById(R.id.txtSearch);
		mSearhResultText = (TextView)findViewById(R.id.txtResult);
		progressBar = (ProgressBar)findViewById(R.id.progress);
		// get the action bar
		actionBar = getActionBar();

		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Search");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
//		mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				// TODO Auto-generated method stub
//				 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//			            search();
//			            return true;
//			        }
//				return false;
//			}
//		});
		
		handleIntent(getIntent());
		
		
			//mLayout = (LinearLayout) view.findViewById(R.id.layout_expert_partners);
			mListView = (ExpandableHeightGridView) findViewById(R.id.list);
			//mNoDataLayout = (TextView)findViewById(R.id.layoutNoItems);
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				 public void onItemClick(AdapterView parent, View v, int position, long id) {
		            	Intent i = new Intent(SearchFilmResultActivity.this, FilmActivity.class);
		            	
		        		i.putExtra("film_id", films.get(position).getId());
		        		i.putExtra("film_name",  films.get(position).getName());
		        		startActivity(i);
		            }
			});
			mListView.setOnScrollListener(new OnScrollListener() {
	        	public void onScrollStateChanged(AbsListView view, int scrollState) { 
	        		
	        	}
	        	public void onScroll(AbsListView view, int firstVisibleItem,
	        		int visibleItemCount, int totalItemCount) {
	        		try {
	        			//Log.i("ON SCROLL", "Start --------------------------");
		        	    if((firstVisibleItem + visibleItemCount == totalItemCount) && !loadingMore &&  totalItemCount < page.GetCount() && currentPage < page.GetPageCount()) {
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
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		//searchView.setFocusable(true);
		searchView.setIconified(false);
		//searchView.setIconifiedByDefault(true);
		searchView.setQuery(keyword, false);
		searchView.setQueryHint("Enter your keyword");
		  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				//search(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				//search(newText);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case android.R.id.home:
	       finish();
	        return true;
		case R.id.action_search:
			// search action
			//keyword = searchView.getQuery().toString();
			//search(keyword);
			return true;
		
		case R.id.action_help:
			// help action
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			keyword = query;
			//mSearchText.setText(keyword);
			/**
			 * Use this query to display search results like 
			 * 1. Getting the data from SQLite and showing in listview 
			 * 2. Making webrequest and displaying the data 
			 * For now we just display the query only
			 */
			progressBar.setVisibility(View.VISIBLE);
			
			new GetDataTask().execute();
			hideKeyboard();
		}

	}
	
	
	
	private void search(String keyword){
		//keyword = keyword;
		//keyword = mSearchText.getText().toString();
		progressBar.setVisibility(View.VISIBLE);
		mSearhResultText.setVisibility(View.GONE);
		new GetDataTask().execute();
		hideKeyboard() ;
	}
	private class GetDataTask extends AsyncTask<Void, Void, Integer> {
		
		

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				films = mFilms.searchFilm(keyword, currentPage);
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
//					if (films.size() > 0) {
//						mNoDataLayout.setVisibility(View.GONE);
//					} else {
//						mNoDataLayout.setVisibility(View.VISIBLE);
//					}
					adapter = new FilmAdatper(SearchFilmResultActivity.this, films);
					mListView.setAdapter(adapter);
					actionBar.setTitle("Search \"" + keyword + "\"");
					mSearhResultText.setText("Total Results: " + mFilms.GetPage().GetCount() +" films");
					mSearhResultText.setVisibility(View.VISIBLE);
				}
				else {
					//mNoDataLayout.setVisibility(View.VISIBLE);
					mSearhResultText.setText("No Film found!");
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			progressBar.setVisibility(View.GONE);
			//searchView.setFocusable(false);
			hideKeyboard();
		}
	}
	private Runnable loadMoreListItems = new Runnable() {
		public void run() {
			loadingMore = true;
			try {
			
				currentPage++;
				moreList = mFilms.searchFilm(keyword, currentPage);
				runOnUiThread(returnRes);
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
	  public void hideKeyboard() {
		  	
	    	getWindow().setSoftInputMode(
	      	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    }
}
