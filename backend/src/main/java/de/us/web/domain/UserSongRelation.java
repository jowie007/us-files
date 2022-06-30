package de.us.web.domain;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserSongRelation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;
	@ManyToOne(cascade = CascadeType.MERGE)
	private Song song;
	private Integer rating;
	private Timestamp ratingDate;
	private Timestamp downloadDate;
	private Boolean favorite;
	private Timestamp favoriteDate;
	
	public UserSongRelation() {

	}

	public UserSongRelation(User user, Song song, int rating, Timestamp ratingDate, Timestamp downloadDate,
			boolean favorite, Timestamp favoriteDate) {
		super();
		this.user = user;
		this.song = song;
		this.rating = rating;
		this.ratingDate = ratingDate;
		this.downloadDate = downloadDate;
		this.favorite = favorite;
		this.favoriteDate = favoriteDate;
	}

	public UserSongRelation(User user, Song song) {
		super();
		this.user = user;
		this.song = song;
		this.rating = 0;
		this.ratingDate = null;
		this.downloadDate = null;
		this.favorite = false;
		this.favoriteDate = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getRating() {
		if(rating == null) {
			rating = 0;
		}
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Timestamp getRatingDate() {
		return ratingDate;
	}

	public void setRatingDate(Timestamp ratingDate) {
		this.ratingDate = ratingDate;
	}

	public Timestamp getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Timestamp downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Boolean isFavorite() {
		if(favorite == null) {
			favorite = false;
		}
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public Timestamp getFavoriteDate() {
		return favoriteDate;
	}

	public void setFavoriteDate(Timestamp favoriteDate) {
		this.favoriteDate = favoriteDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(downloadDate, favorite, favoriteDate, id, rating, ratingDate, song, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSongRelation other = (UserSongRelation) obj;
		return Objects.equals(downloadDate, other.downloadDate) && favorite == other.favorite
				&& Objects.equals(favoriteDate, other.favoriteDate) && Objects.equals(id, other.id)
				&& rating == other.rating && Objects.equals(ratingDate, other.ratingDate)
				&& Objects.equals(song, other.song) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "UserSongRelation [id=" + id + ", song=" + song + ", user=" + user + ", rating=" + rating
				+ ", ratingDate=" + ratingDate + ", downloadDate=" + downloadDate + ", favorite=" + favorite
				+ ", favoriteDate=" + favoriteDate + "]";
	}
}
