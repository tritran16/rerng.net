package net.rerng;

import net.rerng.models.Film;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilmInfoFragment extends Fragment {
	Film film;
	public FilmInfoFragment(){}
	
	public static FilmInfoFragment newInstance(Film film){
		FilmInfoFragment fragment = new FilmInfoFragment();
		fragment.film = film;
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_film_info, container, false);
         
        return rootView;
    }
}
