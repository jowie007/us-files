package de.us.web.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Song {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(cascade = CascadeType.MERGE)
	private User creator;
	private String artist;
	private String title;
	private String version;
	// , orphanRemoval = true, fetch = FetchType.EAGER
	@ManyToMany()
	private Set<User> editors;
	// TODO Set CascadeType
	@ManyToOne
	private Song parentSong;
	private String link;
	private String genre;
	private String language;
	private Integer percentage;
	private Integer releaseYear;
	private Timestamp finishedDate;
	private Timestamp updatedDate;

	public Song() {

	}

	public Song(User creator, String artist, String title, String version, Set<User> editors, Song parentSong,
			String link, Integer percentage, String genre, String language, Integer releaseYear, Timestamp finishedDate,
			Timestamp updatedDate) {
		super();
		this.creator = creator;
		this.artist = artist;
		this.title = title;
		this.version = version;
		this.parentSong = parentSong;
		if (editors != null) {
			this.editors = editors;
		} else {
			this.editors = new HashSet<User>();
		}
		this.link = link;
		this.percentage = percentage;
		this.setGenre(genre);
		this.setLanguage(language);
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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
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

	public Song getParentSong() {
		return parentSong;
	}

	public void setParentSong(Song parentSong) {
		this.parentSong = parentSong;
	}

	public Set<User> getEditors() {
		return editors;
	}

	public void addEditor(User editor) {
		this.editors.add(editor);
	}

	public void setEditors(Set<User> editors) {
		this.editors = editors;
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

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artist, creator, editors, finishedDate, genre, id, language, link, parentSong, percentage,
				releaseYear, title, updatedDate, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(creator, other.creator)
				&& Objects.equals(editors, other.editors) && Objects.equals(finishedDate, other.finishedDate)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(link, other.link)
				&& Objects.equals(parentSong, other.parentSong) && Objects.equals(percentage, other.percentage)
				&& releaseYear == other.releaseYear && Objects.equals(title, other.title)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", creator=" + creator + ", artist=" + artist + ", title=" + title + ", version="
				+ version + ", editors=" + editors + ", parentSong=" + parentSong + ", link=" + link + ", genre="
				+ genre + ", language=" + language + ", percentage=" + percentage + ", releaseYear=" + releaseYear
				+ ", finishedDate=" + finishedDate + ", updatedDate=" + updatedDate + "]";
	}

}
