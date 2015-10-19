package net.rerng.services;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.rerng.utils.Constant;
import net.rerng.utils.Json;

import net.rerng.models.Film;
import net.rerng.models.Group;
import net.rerng.models.Page;
import net.rerng.models.PartFilm;

public class Films {
private Page page;
	
	public Page GetPage() {
		return page;
	}
	
	public void SetPage(Page page) {
		this.page = page;
	}
	
	public ArrayList<Film> getFilmsByGenre(String genre_id, int current_page){
		ArrayList<Film> films = new ArrayList<Film>();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_GENRE_FILMS.replace("{genre_id}", genre_id).replace("{page}", String.valueOf(current_page));
			
		   String jsonStr = json.postData(url, data);
			 //Log.i("getFilmsByGenre Respone String ", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 //Log.i("Status Json", status);
			 
			 String message = jobject.getAsJsonPrimitive("message").toString();
			 //Log.i("Message Json", message);
			 JsonObject  jobject_paginate = jobject.getAsJsonObject("Page");
	         this.page = gson.fromJson(jobject_paginate.toString(), Page.class);
	         
			 if(status.contains("OK") && message.contains("Successful")){
				 JsonArray jarray = jobject.getAsJsonArray("results");
				 if(jarray!=null) {
					//Log.i("jarray.size()", " ======> "+jarray.size()); 
		        	for (int i = 0; i<jarray.size(); i++) {
		        		JsonObject jobject_entity = jarray.get(i).getAsJsonObject();
			        	jobject_entity = jobject_entity.getAsJsonObject("Film");
			        	//Log.i("Film Json String", jobject_entity.toString());
			        	Film film  = new Film();
			        	film = gson.fromJson(jobject_entity.toString(), Film.class);
			        	//Log.i("getHotFilms ", "large image" + film.getLargeImage());
			        	films.add(film);
		        	}
				 }
				 else {
					Log.i("JARRAY", "NULL---------------------");
				 }
				 return films;
			        	
			 }
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Film> getFilmsByGroup(String group_id, int current_page){
		ArrayList<Film> films = new ArrayList<Film>();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_GROUP_FILMS.replace("{group_id}", group_id).replace("{page}", String.valueOf(current_page));
			
		   String jsonStr = json.postData(url, data);
			 Log.i("getFilmsByGenre Respone String ", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 //Log.i("Status Json", status);
			 
			 String message = jobject.getAsJsonPrimitive("message").toString();
			 //Log.i("Message Json", message);
			 JsonObject  jobject_paginate = jobject.getAsJsonObject("Page");
	         this.page = gson.fromJson(jobject_paginate.toString(), Page.class);
	         
			 if(status.contains("OK") && message.contains("Successful")){
				 JsonArray jarray = jobject.getAsJsonArray("results");
				 if(jarray!=null) {
					//Log.i("jarray.size()", " ======> "+jarray.size()); 
		        	for (int i = 0; i<jarray.size(); i++) {
		        		JsonObject jobject_entity = jarray.get(i).getAsJsonObject();
			        	jobject_entity = jobject_entity.getAsJsonObject("Film");
			        	//Log.i("Film Json String", jobject_entity.toString());
			        	Film film  = new Film();
			        	film = gson.fromJson(jobject_entity.toString(), Film.class);
			        	//Log.i("getHotFilms ", "large image" + film.getLargeImage());
			        	films.add(film);
		        	}
				 }
				 else {
					Log.i("JARRAY", "NULL---------------------");
				 }
				 return films;
			        	
			 }
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Film> searchFilm(String keyword, int current_page){
		ArrayList<Film> films = new ArrayList<Film>();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_SEARCH_FILMS.replace("{keyword}", URLEncoder.encode(keyword)).replace("{page}", String.valueOf(current_page));
			Log.i("searchFilm URL ", url);
		   String jsonStr = json.postData(url, data);
			 Log.i("searchFilm Respone String ", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 //Log.i("Status Json", status);
			 
			 String message = jobject.getAsJsonPrimitive("message").toString();
			 //Log.i("Message Json", message);
			 JsonObject  jobject_paginate = jobject.getAsJsonObject("Page");
	         this.page = gson.fromJson(jobject_paginate.toString(), Page.class);
	         
			 if(status.contains("OK") && message.contains("Successful")){
				 JsonArray jarray = jobject.getAsJsonArray("results");
				 if(jarray!=null) {
					//Log.i("jarray.size()", " ======> "+jarray.size()); 
		        	for (int i = 0; i<jarray.size(); i++) {
		        		JsonObject jobject_entity = jarray.get(i).getAsJsonObject();
			        	jobject_entity = jobject_entity.getAsJsonObject("Film");
			        	//Log.i("Film Json String", jobject_entity.toString());
			        	Film film  = new Film();
			        	film = gson.fromJson(jobject_entity.toString(), Film.class);
			        	//Log.i("getHotFilms ", "large image" + film.getLargeImage());
			        	films.add(film);
		        	}
				 }
				 else {
					Log.i("JARRAY", "NULL---------------------");
				 }
				 return films;
			        	
			 }
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList<Film> getLastedFilm(){
		
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_LASTED_FILMS;
			Film film  = new Film();
			 String jsonStr = json.postData(url, data);
			 Log.i("Json Respone String ", jsonStr);
			
			 
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public ArrayList<Film> getHotFilms(){
		ArrayList<Film> films = new ArrayList<Film>();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_HOT_FILMS;
			
		   String jsonStr = json.postData(url, data);
			 Log.i("Json Respone String ", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 //Log.i("Status Json", status);
			 
			 String message = jobject.getAsJsonPrimitive("message").toString();
			 //Log.i("Message Json", message);
			 if(status.contains("OK") && message.contains("Successful")){
				 JsonArray jarray = jobject.getAsJsonArray("results");
				 if(jarray!=null) {
					//Log.i("jarray.size()", " ======> "+jarray.size()); 
		        	for (int i = 0; i<jarray.size(); i++) {
		        		JsonObject jobject_entity = jarray.get(i).getAsJsonObject();
			        	jobject_entity = jobject_entity.getAsJsonObject("Film");
			        	//Log.i("Film Json String", jobject_entity.toString());
			        	Film film  = new Film();
			        	film = gson.fromJson(jobject_entity.toString(), Film.class);
			        	//Log.i("getHotFilms ", "large image" + film.getLargeImage());
			        	films.add(film);
		        	}
				 }
				 else {
					//Log.i("JARRAY", "NULL---------------------");
				 }
				 return films;
			        	
			 }
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public Film getFilmInfo(String id){
		Film film  = new Film();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_FILM_INFO.replace("{id}", id) ;
			Log.i("Json Url ", url);
			 String jsonStr = json.postData(url, data);
			 Log.i("Json Respone String ", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 if(status.contains("OK")){
				 jobject= jobject.getAsJsonObject("result");
				 JsonObject jobject_film = jobject.getAsJsonObject("Film");
				Log.i("Json Film", jobject_film.toString());
	        	film = gson.fromJson(jobject_film.toString(), Film.class);
	        	
	        	JsonArray j_part_array = jobject.getAsJsonArray("Part");
	        	
	        	ArrayList<PartFilm> parts = new ArrayList<PartFilm>();
	        	for (int j = 0; j<j_part_array.size(); j++) {
	        		JsonObject jobject_part = j_part_array.get(j).getAsJsonObject();
	        		//jobject_part = jobject_part.getAsJsonObject("Film");
		        	//Log.i("Film Json String", jobject_entity.toString());
		        	PartFilm part  = new PartFilm();
		        	part = gson.fromJson(jobject_part.toString(), PartFilm.class);
		        	
		        	parts.add(part);
		        	
	        	}
	        	film.setParts(parts);
	        	return film;
			 }
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList<Group> getGroupFilms(){
		ArrayList<Group> groups = new ArrayList<Group>();
		Json json = new Json();
        Gson gson = new Gson();
        List<NameValuePair> data = new ArrayList<NameValuePair>();
		try {
			String url = Constant.URL_MENU_NAVIGATION;
			
		   String jsonStr = json.postData(url, data);
			 Log.i("getGroupFilms Json", jsonStr);
			 JsonElement jelement = new JsonParser().parse(jsonStr);
			 
			 JsonObject  jobject = jelement.getAsJsonObject();
			 String status = jobject.getAsJsonPrimitive("status").toString();
			 
			 
			 String message = jobject.getAsJsonPrimitive("message").toString();
			 
			 if(status.contains("OK")){
				 JsonArray jarray = jobject.getAsJsonArray("results");
				 if(jarray!=null) {
					Log.i("getGroupFilms jarray.size()", " ======> "+jarray.size()); 
		        	for (int i = 0; i<jarray.size(); i++) {
		        		JsonObject jobject_entity = jarray.get(i).getAsJsonObject();
			        	//jobject_entity = jobject_entity.getAsJsonObject("Group");
			        	
			        	Group group = new Group(); 
			        	group = gson.fromJson(jobject_entity.toString(), Group.class);
			        	
			        	JsonArray j_film_array = jobject_entity.getAsJsonArray("Films");
			        	
			        	ArrayList<Film> films = new ArrayList<Film>();
			        	for (int j = 0; j<j_film_array.size(); j++) {
			        		JsonObject jobject_film = j_film_array.get(j).getAsJsonObject();
			        		jobject_film = jobject_film.getAsJsonObject("Film");
				        	//Log.i("Film Json String", jobject_entity.toString());
				        	Film film  = new Film();
				        	film = gson.fromJson(jobject_film.toString(), Film.class);
				        	
				        	films.add(film);
			        	}
			        	group.setFilms(films);
			        	groups.add(group);
		        	}
				 }
				 else {
					 Log.i("JARRAY", "NULL---------------------");
				 }
				 return groups;
			        	
			 }
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
