package net.rerng.adapter;


import net.rerng.FilmCommentsFragment;
import net.rerng.FilmInfoFragment;
import net.rerng.FilmRelatedFragment;
import net.rerng.models.Film;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FilmInfoFragmentAdapter extends FragmentPagerAdapter {
	protected static final String[] CONTENT = new String[] { "Information", "Reated" };
	protected static Fragment[] FRAGMENTS = null;

	private int mCount = CONTENT.length;

	public FilmInfoFragmentAdapter(FragmentManager fm, Film film) {
		super(fm);
		FRAGMENTS = new Fragment[] { FilmInfoFragment.newInstance(film),
				FilmRelatedFragment.newInstance(film)
			};
	}

	@Override
	public Fragment getItem(int position) {
		return FRAGMENTS[position % CONTENT.length];
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return FilmInfoFragmentAdapter.CONTENT[position % CONTENT.length];
	}

	public FilmInfoFragment getFilmInfoFragment() {
		return (FilmInfoFragment) FRAGMENTS[0];
	}

	public FilmRelatedFragment getAnswerFragment() {
		return (FilmRelatedFragment) FRAGMENTS[1];
	}

	public FilmCommentsFragment getPartnerFragment() {
		return (FilmCommentsFragment) FRAGMENTS[2];
	}
	
	/*
	 * @Override
	 * 
	 * public int getIconResId(int index) { //return ICONS[index %
	 * ICONS.length]; return 0; }
	 */
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}
