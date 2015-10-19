package net.rerng;


import java.util.ArrayList;

import net.rerng.models.Page;

import net.rerng.adapter.NavDrawerListAdapter;
import net.rerng.models.Film;
import net.rerng.models.NavDrawerItem;
import net.rerng.services.Films;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends MenuNavigationActivity {
	ArrayList<Film> films  = new ArrayList<Film>();
	//private ProgressBar mProgress;
	private int currentPage = 1;
	private Page page = new Page();
	Films mFilms = new Films();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createMenuNaviagtion(savedInstanceState);
		
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setQueryHint("Enter your keyword");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setCustomTitle(CharSequence title) {
		ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_title, null);
        TextView mTitle = (TextView)view.findViewById(R.id.title);
        mTitle.setText(title);
        ab.setCustomView(view);
	}
}
