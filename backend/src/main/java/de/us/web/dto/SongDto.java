package de.us.web.dto;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SongDto {

	@NotNull
	private Long id;
	@NotNull
	private UserDto creator;
	@NotNull
	@Size(min = 1, max = 50)
	private String artist;
	@NotNull
	@Size(min = 1, max = 50)
	private String title;
	@NotNull
	@Size(min = 1, max = 50)
	private String version;
	private Set<UserDto> editors;
	private SongDto parentSongDto;
	private String link;
	private Integer percentage;
	private String genre;
	private String language;
	private int releaseYear;
	private Timestamp finishedDate;
	private Timestamp updatedDate;

	public SongDto(UserDto creator, String artist, String title, String version, Set<UserDto> editors,
			SongDto parentSongDto, String link, Integer percentage, String genre, String language, int releaseYear,
			Timestamp finishedDate, Timestamp updatedDate) {
		super();
		this.creator = creator;
		this.artist = artist;
		this.title = title;
		this.version = version;
		this.editors = editors;
		this.parentSongDto = parentSongDto;
		this.link = link;
		this.percentage = percentage;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
		this.finishedDate = finishedDate;
		this.updatedDate = updatedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDto getCreator() {
		return creator;
	}

	public void setCreator(UserDto creator) {
		this.creator = creator;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<UserDto> getEditors() {
		return editors;
	}

	public void setEditors(Set<UserDto> editors) {
		this.editors = editors;
	}

	public SongDto getParentSongDto() {
		return parentSongDto;
	}

	public void setParentSongDto(SongDto parentSongDto) {
		this.parentSongDto = parentSongDto;
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

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, creator, editors, finishedDate, genre, id, language, link, parentSongDto,
				percentage, releaseYear, title, updatedDate, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongDto other = (SongDto) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(creator, other.creator)
				&& Objects.equals(editors, other.editors) && Objects.equals(finishedDate, other.finishedDate)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(link, other.link)
				&& Objects.equals(parentSongDto, other.parentSongDto) && Objects.equals(percentage, other.percentage)
				&& releaseYear == other.releaseYear && Objects.equals(title, other.title)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "SongDto [id=" + id + ", creator=" + creator + ", artist=" + artist + ", title=" + title + ", version="
				+ version + ", editors=" + editors + ", parentSongDto=" + parentSongDto + ", link=" + link
				+ ", percentage=" + percentage + ", genre=" + genre + ", language=" + language + ", releaseYear="
				+ releaseYear + ", finishedDate=" + finishedDate + ", updatedDate=" + updatedDate + "]";
	}

}