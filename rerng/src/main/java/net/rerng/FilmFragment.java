package net.rerng;

import java.util.ArrayList;
import com.viewpagerindicator.TabPageIndicator;

import net.rerng.adapter.FilmAdatper;
import net.rerng.adapter.FilmInfoFragmentAdapter;
import net.rerng.models.Film;
import net.rerng.models.PartFilm;
import net.rerng.services.Films;
import net.rerng.utils.Constant;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class FilmFragment extends Fragment {
	String film_id;
	String film_name;
	String part_id;
	PartFilm part;
	int part_index = 0;
	Film film;
	Films mFilms = new Films();
	ProgressBar progressBar;
	 VideoView videoView;
	 
	public FilmFragment(){}
	public static FilmFragment newInstance(String film_id, String film_name){
		FilmFragment fragment = new FilmFragment();
		fragment.film_id = film_id;
		fragment.film_name = film_name;
		return fragment;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        
        ((MenuNavigationActivity)getActivity()).setTitle(film_name);
		//Use a media controller so that you can scroll the video contents
		//and also to pause, start the video.
		MediaController mediaController = new MediaController(getActivity());
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		progressBar.setVisibility(View.VISIBLE);
		
//		FilmInfoFragmentAdapter mAdapter = new FilmInfoFragmentAdapter(getActivity().getSupportFragmentManager(), film);
//		ViewPager mPager = (ViewPager)rootView.findViewById(R.id.pager);
//        mPager.setAdapter(mAdapter);
//       
//        TabPageIndicator mIndicator = (TabPageIndicator)rootView.findViewById(R.id.indicator);
//        mIndicator.setViewPager(mPager);
    	new GetFilmInfoTask().execute();
        return rootView;
    }
	
	private void startPlayFilm(PartFilm part){
		
		String videoUrl = "";
		if(part.getServer().equalsIgnoreCase("local")) videoUrl = Constant.URL + "/files/films/" + part.getFilename();
		else videoUrl = part.getFull_path();
		Log.i("FilmFragment Video URL", videoUrl);
		videoView.setVideoURI(Uri.parse(videoUrl));
	    videoView.start();
	}
	
	private class GetFilmInfoTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				film = mFilms.getFilmInfo(film_id);
			} catch (Exception ex) {
				//Log.e(TAG, "Error on doInBackground-GetPartnersTask", ex);
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			try {
				if(film != null){
					if (film.getParts().size() > 0){
						part= film.getParts().get(part_index);
						startPlayFilm(part);
					}
				}
					
				
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			progressBar.setVisibility(View.GONE);
		}
	}
}
