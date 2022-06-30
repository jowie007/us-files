package de.us.web.payload.request;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class UserSongRelationRequest {

	@NotNull
	private String userName;
	@NotNull
	private Long songId;
	@Range(min = 0, max = 5)
	private Integer rating;
	private Boolean downloaded;
	private Boolean favorite;

	public UserSongRelationRequest(@NotNull String userName, @NotNull Long songId,
			@Range(min = 0, max = 5) Integer rating, Boolean downloaded, Boolean favorite) {
		super();
		this.userName = userName;
		this.songId = songId;
		this.rating = rating;
		this.downloaded = downloaded;
		this.favorite = favorite;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getSongId() {
		return songId;
	}

	public void setSongId(Long songId) {
		this.songId = songId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Boolean getDownloaded() {
		if(downloaded == null) {
			downloaded = false;
		}
		return downloaded;
	}

	public void setDownloaded(Boolean downloaded) {
		this.downloaded = downloaded;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	@Override
	public int hashCode() {
		return Objects.hash(downloaded, favorite, rating, songId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSongRelationRequest other = (UserSongRelationRequest) obj;
		return Objects.equals(downloaded, other.downloaded) && Objects.equals(favorite, other.favorite)
				&& Objects.equals(rating, other.rating) && Objects.equals(songId, other.songId)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "UserSongRelationRequest [userName=" + userName + ", songId=" + songId + ", rating=" + rating
				+ ", downloaded=" + downloaded + ", favorite=" + favorite + "]";
	}

}
