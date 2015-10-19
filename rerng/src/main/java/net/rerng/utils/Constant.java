package net.rerng.utils;

public class Constant {
	public final static String URL = "http://rerng.net";
	public final static String API_URL = URL + "/api";
	
	public final static String URL_FILM_INFO = API_URL + "/get_film_info/{id}.json";
	public final static String URL_LASTED_FILMS = API_URL + "/get_last_films/page:{page}.json";
	public final static String URL_GROUP_FILMS = API_URL + "/get_films_by_group/{group_id}/page:{page}.json";
	public final static String URL_HOT_FILMS = API_URL + "/get_films_by_group/4.json";
	public final static String URL_GENRE_FILMS = API_URL + "/get_films_by_genre/{genre_id}/page:{page}.json";
	public final static String URL_MENU_NAVIGATION = API_URL + "/get_menu_navigation.json";
	
	public final static String URL_SEARCH_FILMS = API_URL + "/search/{keyword}/page:{page}.json";
	
	
	public static final String YOUTUBE_DEVELOPER_KEY = "AI39si47PPHEel5xtB8oIqwylJu1ngk7yfSFvPhTn4Jve9dGEr63Es6VIutkfHir3eizimrexLQZb8h72esccSlOK3NrDbubxw";
}
