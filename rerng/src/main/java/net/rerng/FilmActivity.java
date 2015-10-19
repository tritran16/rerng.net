package net.rerng;

import java.io.IOException;

import net.rerng.models.Film;
import net.rerng.models.PartFilm;
import net.rerng.services.Films;
import net.rerng.utils.Constant;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import net.rerng.controls.VideoControllerView;

public class FilmActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
	ProgressBar progressBar;
	 //VideoView videoView;
	 String film_id;
	 String film_name;
	 
	 Film film = null;
	 Films mFilms = new Films();
	 PartFilm part;
	 int part_index = 0;
	
	SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    boolean isFullScreen = false;
    ActionBar actionBar;

    int stopPosition =0;
    boolean isPause = false;
    
	// Refresh menu item
	private MenuItem refreshMenuItem;
	
	TextView name;
	TextView desc;
	TextView actor;
	TextView director;
	TextView writer;
	LinearLayout layoutMain;
	LinearLayout layoutInfo;
	
	String videoUrl = "";
	
	protected PowerManager.WakeLock mWakeLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_film);
		progressBar = (ProgressBar)findViewById(R.id.progress);
		layoutMain = (LinearLayout)findViewById(R.id.layoutVideo);
		layoutInfo = (LinearLayout)findViewById(R.id.layoutInfo);
       name = (TextView)findViewById(R.id.txtFilmName);
       desc = (TextView)findViewById(R.id.txtFilmDesc);
       desc.setMovementMethod(new ScrollingMovementMethod());
       actor = (TextView)findViewById(R.id.txtActor);
       director = (TextView)findViewById(R.id.txtDirector);
       writer = (TextView)findViewById(R.id.txtWriter);
		//Use a media controller so that you can scroll the video contents
		//and also to pause, start the video.
//		MediaController mediaController = new MediaController(this);
//		mediaController.setAnchorView(videoView);
//		videoView.setMediaController(mediaController);

		
		actionBar = getActionBar();
		
		Intent i = getIntent();
		film_id = i.getStringExtra("film_id");
		film_name = i.getStringExtra("film_name");
		// Hide the action bar title
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(film_name);
		actionBar.setDisplayHomeAsUpEnabled(true);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        
        if( savedInstanceState != null ) {
	        stopPosition = savedInstanceState.getInt("position");
	        Log.i("Save Instance ", "Stop position " + stopPosition);
	        isPause = savedInstanceState.getBoolean("is_pause");
	        Toast.makeText(this, "Stop at :" + stopPosition, Toast.LENGTH_LONG).show();
	    }
        
        //keep screen alway on
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        
		progressBar.setVisibility(View.VISIBLE);
		new GetFilmInfoTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.film, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setQueryHint("Enter your keyword");
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
			
			return true;
		
		case R.id.action_help:
			// help action
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	

	/*
	 * Actionbar navigation item select listener
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// Action to be taken after selecting a spinner item
		return false;
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
				Log.e("doInBackground", "Error on doInBackground-GetPartnersTask", ex);
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			try {
				if(film != null){
					name.setText(film_name);
					desc.setText(film.getDescription()!=null||!film.getDescription().trim().equals("")?Html.fromHtml(film.getDescription()) : "Not Available");
					actor.setText((film.getActor()!=null|| !film.getActor().trim().equals("")) ? film.getActor():"Unknowns");
					writer.setText((film.getWriters()!=null|| !film.getWriters().trim().equals("")) ? film.getWriters():"Unknowns");
					director.setText((film.getDirector()!=null || !film.getDirector().trim().equals("")) ? film.getDirector():"Unknowns");
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
	
	private void startPlayFilm(PartFilm part){
		layoutMain.setVisibility(View.VISIBLE);
		//String videoUrl = "";
		if(part.getServer().equalsIgnoreCase("local")) videoUrl = Constant.URL + "/files/films/" + part.getFilename();
		else videoUrl = part.getFull_path();
		Log.i("FilmFragment Video URL", videoUrl);
		try {
		 
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	        player.setDataSource(this, Uri.parse(videoUrl));
	        player.prepareAsync();
	        player.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		videoView.setVideoURI(Uri.parse(videoUrl));
//		videoView.setVisibility(View.VISIBLE);
//		videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//			
//			@Override
//			public boolean onError(MediaPlayer mp, int what, int extra) {
//				// TODO Auto-generated method stub
//				videoView.setVisibility(View.GONE);
//				return false;
//			}
//		});
	    //videoView.start();
	    
		show_others();
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
//        params.width =  (int) (300*metrics.density);
        params.height = (int) (250*metrics.density);
//        params.leftMargin = 30;
        params.width =  FrameLayout.LayoutParams.FILL_PARENT;
        
       // params.height =  FrameLayout.LayoutParams.WRAP_CONTENT;
        videoSurface.setLayoutParams(params);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    
	    stopPosition =  player.getCurrentPosition();
	    player.pause();
	    player.stop();
	    Log.i("onSaveInstanceState", "Stop At : " + stopPosition);
	    outState.putInt("position", stopPosition);
	    outState.putBoolean("is_pause", true);
	    
	    super.onSaveInstanceState(outState);
	   
	} 
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    stopPosition = savedInstanceState.getInt("position");
        Log.i("Save Instance ", "Stop position " + stopPosition);
        isPause = savedInstanceState.getBoolean("is_pause");
        Toast.makeText(this, "Stop at :" + stopPosition, Toast.LENGTH_LONG).show();
	    // Restore state members from saved instance
	   
	}
	@Override
	public void onResume() {
	    super.onResume();
	    Log.i("onResume", "Resume At : " + stopPosition);
	   
//	    try {
//			player = new MediaPlayer();
//			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//	        player.setDataSource(this, Uri.parse(videoUrl));
//	        player.prepareAsync();
//	        player.setOnPreparedListener(this);
//		} catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//	    
//	    player.start(); //Or use resume() if it doesn't work. I'm not sure
	    
	    SurfaceHolder videoHolder = videoSurface.getHolder();
        
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        if(film == null){
        	progressBar.setVisibility(View.VISIBLE);
    		new GetFilmInfoTask().execute();
        }
        else {
        	try {
       		 
    			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	        player.setDataSource(this, Uri.parse(videoUrl));
    	        player.prepareAsync();
    	        player.setOnPreparedListener(this);
    	        player.seekTo(stopPosition);
    	        controller.show();
    		} catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        if(isFullScreen){
        	hide_others();
        	
        }
        else {
        	show_others();
        }
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	player.setDisplay(holder);
       
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        controller.show();
        if(isPause){
        	player.start();
        	isPause = false;
        }
        player.seekTo(stopPosition);
        //player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
    	Log.i("Video Player", "PAUSE ----------------------------");
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
    	isPause = false;
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen() {
        Toast.makeText(this, "toogle Full Screen", Toast.LENGTH_LONG).show();
        if(!isFullScreen){
//        	actionBar.hide();
//	        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//	        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
//	        params.width =  metrics.widthPixels;
//	        params.height = metrics.heightPixels;
//	        params.leftMargin = 0;
//	        videoSurface.setLayoutParams(params);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
//        	actionBar.show();
//        	 DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//             android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
//             params.width =  (int) (300*metrics.density);
//             params.height = (int) (250*metrics.density);
//             params.leftMargin = 30;
//             videoSurface.setLayoutParams(params);
             setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isFullScreen = !isFullScreen;
    }
    // End VideoMediaController.MediaPlayerControl
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
 
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	hide_others();
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        	DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
	        params.width =  metrics.widthPixels;
	        params.height = metrics.heightPixels;
	        params.leftMargin = 0;
	        videoSurface.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            show_others();
            DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
//            params.width =  (int) (300*metrics.density);
            params.height = (int) (250*metrics.density);
//            params.leftMargin = 30;
            params.width =  FrameLayout.LayoutParams.FILL_PARENT;
            
           // params.height =  FrameLayout.LayoutParams.WRAP_CONTENT;
            videoSurface.setLayoutParams(params);
        }
    }
    
    void hide_others(){
    	actionBar.hide();
    	layoutInfo.setVisibility(View.GONE);
    	 getWindow().getDecorView().setSystemUiVisibility(
    			 View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                 | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    
    void show_others(){
    	actionBar.show();
    	layoutInfo.setVisibility(View.VISIBLE);
    	getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    
    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

}
