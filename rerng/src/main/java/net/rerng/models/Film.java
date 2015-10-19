package net.rerng.models;

import java.util.List;

public class Film {
	String id;
	String name;
	String slug;
	String image;
	String large_image;
	String views;
	String actor;
	String director;
	String writers;
	String published_date;
	String created_date;
	List<Genre> genres;
	String number_part;
	List<PartFilm> parts;
	String description;
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
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getActor() {
		return actor;
	}
	public void setActors(String actors) {
		this.actor = actors;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String directors) {
		this.director = directors;
	}
	public String getWriters() {
		return writers;
	}
	public void setWriters(String writers) {
		this.writers = writers;
	}
	public String getPublished_date() {
		return published_date;
	}
	public void setPublished_date(String published_date) {
		this.published_date = published_date;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public String getNumber_part() {
		return number_part;
	}
	public void setNumber_part(String number_part) {
		this.number_part = number_part;
	}
	public List<PartFilm> getParts() {
		return parts;
	}
	public void setParts(List<PartFilm> parts) {
		this.parts = parts;
	}
	public String getLargeImage() {
		return large_image;
	}
	public void setLargeImage(String large_image) {
		this.large_image = large_image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
