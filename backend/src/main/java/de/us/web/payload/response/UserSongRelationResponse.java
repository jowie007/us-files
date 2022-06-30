package de.us.web.payload.response;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import de.us.web.dto.UserSongRelationDto;

public class UserSongRelationResponse {

	@NotNull
	private Long id;
	@NotNull
	private String userName;
	@NotNull
	private SongResponse songResponse;
	@Range(min = 0, max = 5)
	private Integer rating;
	private Timestamp ratingDate;
	private Timestamp downloadDate;
	private Boolean favorite;
	private Timestamp favoriteDate;

	public UserSongRelationResponse(@NotNull String userName, @NotNull SongResponse songResponse,
			@Range(min = 1, max = 5) Integer rating, Timestamp ratingDate, Timestamp downloadDate, Boolean favorite,
			Timestamp favoriteDate) {
		super();
		this.userName = userName;
		this.songResponse = songResponse;
		this.rating = rating;
		this.ratingDate = ratingDate;
		this.downloadDate = downloadDate;
		this.favorite = favorite;
		this.favoriteDate = favoriteDate;
	}
	
	public UserSongRelationResponse(@NotNull String userName, @NotNull SongResponse songResponse) {
		super();
		this.userName = userName;
		this.songResponse = songResponse;
		this.rating = 0;
		this.ratingDate = null;
		this.downloadDate = null;
		this.favorite = false;
		this.favoriteDate = null;
	}
	
	public UserSongRelationResponse(@NotNull UserSongRelationDto userSongRelationDto, @NotNull SongResponse songResponse) {
		super();
		this.userName = userSongRelationDto.getUserDto().getName();
		this.songResponse = songResponse;
		this.rating = userSongRelationDto.getRating();
		this.ratingDate = userSongRelationDto.getRatingDate();
		this.downloadDate = userSongRelationDto.getDownloadDate();
		this.favorite = userSongRelationDto.isFavorite();
		this.favoriteDate = userSongRelationDto.getFavoriteDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SongResponse getSongResponse() {
		return songResponse;
	}

	public void setSongResponse(SongResponse songResponse) {
		this.songResponse = songResponse;
	}

	public Integer getRating() {
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

	public Boolean getFavorite() {
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
		return Objects.hash(downloadDate, favorite, favoriteDate, id, rating, ratingDate, songResponse, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSongRelationResponse other = (UserSongRelationResponse) obj;
		return Objects.equals(downloadDate, other.downloadDate) && Objects.equals(favorite, other.favorite)
				&& Objects.equals(favoriteDate, other.favoriteDate) && Objects.equals(id, other.id)
				&& Objects.equals(rating, other.rating) && Objects.equals(ratingDate, other.ratingDate)
				&& Objects.equals(songResponse, other.songResponse) && Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "UserSongRelationResponse [id=" + id + ", userName=" + userName + ", songResponse=" + songResponse
				+ ", rating=" + rating + ", ratingDate=" + ratingDate + ", downloadDate=" + downloadDate + ", favorite="
				+ favorite + ", favoriteDate=" + favoriteDate + "]";
	}

}
