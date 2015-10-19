package net.rerng;

import net.rerng.models.Film;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmRelatedFragment extends Fragment {
	Film film;
	public FilmRelatedFragment(){}
	
	public static FilmRelatedFragment newInstance(Film film){
		FilmRelatedFragment fragment = new FilmRelatedFragment();
		fragment.film = film;
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_film_related, container, false);
         
        return rootView;
    }
}
