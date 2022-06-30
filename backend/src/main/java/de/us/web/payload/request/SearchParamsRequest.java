package de.us.web.payload.request;

import java.util.Objects;

public class SearchParamsRequest {

	private String creator;
	private String artist;
	private String title;
	private String version;
	private String editor;
	private String genre;
	private String language;
	private Integer releaseYear;

	public SearchParamsRequest(String creator, String artist, String title, String version, String editor, String genre,
			String language, Integer releaseYear) {
		super();
		this.creator = creator;
		this.artist = artist;
		this.title = title;
		this.version = version;
		this.editor = editor;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
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

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
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

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, creator, editor, genre, language, releaseYear, title, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchParamsRequest other = (SearchParamsRequest) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(creator, other.creator)
				&& Objects.equals(editor, other.editor) && Objects.equals(genre, other.genre)
				&& Objects.equals(language, other.language) && releaseYear == other.releaseYear
				&& Objects.equals(title, other.title) && Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "SearchParamsRequest [creator=" + creator + ", artist=" + artist + ", title=" + title + ", version="
				+ version + ", editor=" + editor + ", genre=" + genre + ", language=" + language + ", releaseYear="
				+ releaseYear + "]";
	}

}
