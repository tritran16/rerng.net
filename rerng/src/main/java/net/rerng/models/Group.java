package net.rerng.models;

import java.util.ArrayList;

public class Group {
	String id;
	String name;
	String type;
	ArrayList<Film> films;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Film> getFilms() {
		return films;
	}
	public void setFilms(ArrayList<Film> films) {
		this.films = films;
	}
	
	
}
