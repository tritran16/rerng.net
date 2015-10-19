package net.rerng;

import net.rerng.models.Film;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmCommentsFragment extends Fragment {
	Film film;
	public FilmCommentsFragment(){}
	
	public static FilmCommentsFragment newInstance(Film film){
		FilmCommentsFragment fragment = new FilmCommentsFragment();
		fragment.film = film;
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_film_comments, container, false);
         
        return rootView;
    }
}
