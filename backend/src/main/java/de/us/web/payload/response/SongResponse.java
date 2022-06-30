package de.us.web.payload.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class SongResponse {

	private Long id;
	private String artist;
	private String title;
	private String creator;
	private String version;
	private Set<String> editors;
	private Double averageRating;
	private Integer downloads;
	private Integer ratingCount;
	private String link;
	private Integer percentage;
	private String genre;
	private String language;
	private Integer releaseYear;
	private Timestamp finishedDate;
	private Timestamp updatedDate;
	private SongResponse parentSongResponse;

	public SongResponse(Long id, String artist, String title, String creator, String version, Set<String> editors,
			String link, Integer percentage, String genre, String language, Integer releaseYear, Timestamp finishedDate,
			Timestamp updatedDate, SongResponse parentSongResponse) {
		super();
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.creator = creator;
		this.version = version;
		this.editors = editors;
		this.link = link;
		this.percentage = percentage;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
		this.finishedDate = finishedDate;
		this.updatedDate = updatedDate;
		this.parentSongResponse = parentSongResponse;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<String> getEditors() {
		return editors;
	}

	public void setEditors(Set<String> editors) {
		this.editors = editors;
	}

	public SongResponse getParentSongResponse() {
		return parentSongResponse;
	}

	public void setParentSongResponse(SongResponse parentSongResponse) {
		this.parentSongResponse = parentSongResponse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAverageRating() {
		if (averageRating == null) {
			this.averageRating = 0.0;
		}
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
		this.averageRating = BigDecimal.valueOf(averageRating).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}

	public Integer getDownloads() {
		if (averageRating == null) {
			this.downloads = 0;
		}
		return downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public Timestamp getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Timestamp finishedDate) {
		this.finishedDate = finishedDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, averageRating, creator, downloads, editors, finishedDate, genre, id, language, link,
				parentSongResponse, percentage, ratingCount, releaseYear, title, updatedDate, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongResponse other = (SongResponse) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(averageRating, other.averageRating)
				&& Objects.equals(creator, other.creator) && Objects.equals(downloads, other.downloads)
				&& Objects.equals(editors, other.editors) && Objects.equals(finishedDate, other.finishedDate)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(link, other.link)
				&& Objects.equals(parentSongResponse, other.parentSongResponse)
				&& Objects.equals(percentage, other.percentage) && Objects.equals(ratingCount, other.ratingCount)
				&& Objects.equals(releaseYear, other.releaseYear) && Objects.equals(title, other.title)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "SongResponse [id=" + id + ", artist=" + artist + ", title=" + title + ", creator=" + creator
				+ ", version=" + version + ", editors=" + editors + ", averageRating=" + averageRating + ", downloads="
				+ downloads + ", ratingCount=" + ratingCount + ", link=" + link + ", percentage=" + percentage
				+ ", genre=" + genre + ", language=" + language + ", releaseYear=" + releaseYear + ", finishedDate="
				+ finishedDate + ", updatedDate=" + updatedDate + ", parentSongResponse=" + parentSongResponse + "]";
	}

}
