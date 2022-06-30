package de.us.web.dto;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class UserSongRelationDto {

	@NotNull
	private Long id;
	@NotNull
	private UserDto userDto;
	@NotNull
	private SongDto songDto;
	@Range(min = 0, max = 5)
	private Integer rating;
	private Timestamp ratingDate;
	private Timestamp downloadDate;
	private Boolean favorite;
	private Timestamp favoriteDate;
	
	public UserSongRelationDto() {
		
	}

	public UserSongRelationDto(@NotNull UserDto userDto, @NotNull SongDto songDto,
			@Range(min = 1, max = 5) int rating, Timestamp ratingDate, Timestamp downloadDate, boolean favorite,
			Timestamp favoriteDate) {
		super();
		this.userDto = userDto;
		this.songDto = songDto;
		this.rating = rating;
		this.ratingDate = ratingDate;
		this.downloadDate = downloadDate;
		this.favorite = favorite;
		this.favoriteDate = favoriteDate;
	}

	public UserSongRelationDto(@NotNull UserDto userDto, @NotNull SongDto songDto) {
		super();
		this.userDto = userDto;
		this.songDto = songDto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SongDto getSongDto() {
		return songDto;
	}

	public void setSongDto(SongDto songDto) {
		this.songDto = songDto;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public Integer getRating() {
		if(rating == null) {
			rating = 0;
		}
		return rating;
	}
	
	public Integer getRatingOrNull() {
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
	
	public Boolean isFavoriteOrNull() {
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
		return Objects.hash(downloadDate, favorite, favoriteDate, id, rating, ratingDate, songDto, userDto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSongRelationDto other = (UserSongRelationDto) obj;
		return Objects.equals(downloadDate, other.downloadDate) && favorite == other.favorite
				&& Objects.equals(favoriteDate, other.favoriteDate) && Objects.equals(id, other.id)
				&& rating == other.rating && Objects.equals(ratingDate, other.ratingDate)
				&& Objects.equals(songDto, other.songDto) && Objects.equals(userDto, other.userDto);
	}

	@Override
	public String toString() {
		return "UserSongRelationDto [id=" + id + ", songDto=" + songDto + ", userDto=" + userDto + ", rating=" + rating
				+ ", ratingDate=" + ratingDate + ", downloadDate=" + downloadDate + ", favorite=" + favorite
				+ ", favoriteDate=" + favoriteDate + "]";
	}

}
