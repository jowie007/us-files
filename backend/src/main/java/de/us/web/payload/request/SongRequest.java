package de.us.web.payload.request;

import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class SongRequest {

	private Long id;
	@NotBlank
	private String creatorName;
	@NotBlank
	private String artist;
	@NotBlank
	private String title;
	@NotBlank
	private String version;
	private Set<String> editorNames;
	private Long parentSongId;
	private String link;
	private String genre;
	private String language;
	private int releaseYear;
	private Integer percentage;

	public SongRequest() {
		super();
	}

	public SongRequest(Long id, @NotBlank String creatorName, @NotBlank String artist, @NotBlank String title,
			@NotBlank String version, Set<String> editorNames, Long parentSongId, String link, String genre, String language,
			int releaseYear, Integer percentage) {
		super();
		this.id = id;
		this.creatorName = creatorName;
		this.artist = artist;
		this.title = title;
		this.version = version;
		this.editorNames = editorNames;
		this.parentSongId = parentSongId;
		this.link = link;
		this.genre = genre;
		this.language = language;
		this.releaseYear = releaseYear;
		this.percentage = percentage;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public Set<String> getEditorNames() {
		return editorNames;
	}

	public void setEditorNames(Set<String> editorsNames) {
		this.editorNames = editorsNames;
	}

	public Long getParentSongId() {
		return parentSongId;
	}

	public void setParentSongId(Long parentSongId) {
		this.parentSongId = parentSongId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, creatorName, editorNames, genre, id, language, link, parentSongId, percentage,
				releaseYear, title, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongRequest other = (SongRequest) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(creatorName, other.creatorName)
				&& Objects.equals(editorNames, other.editorNames) && Objects.equals(genre, other.genre)
				&& Objects.equals(id, other.id) && Objects.equals(language, other.language)
				&& Objects.equals(link, other.link) && Objects.equals(parentSongId, other.parentSongId)
				&& Objects.equals(percentage, other.percentage) && releaseYear == other.releaseYear
				&& Objects.equals(title, other.title) && Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "SongRequest [id=" + id + ", creatorName=" + creatorName + ", artist=" + artist + ", title=" + title
				+ ", version=" + version + ", editorNames=" + editorNames + ", parentSongId=" + parentSongId + ", link="
				+ link + ", genre=" + genre + ", language=" + language + ", releaseYear=" + releaseYear
				+ ", percentage=" + percentage + "]";
	}

}
