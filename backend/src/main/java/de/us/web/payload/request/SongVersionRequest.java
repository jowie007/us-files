package de.us.web.payload.request;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

public class SongVersionRequest {

	@NotBlank
	private String artist;
	@NotBlank
	private String title;
	@NotBlank
	private String version;

	public SongVersionRequest(@NotBlank String artist, @NotBlank String title, @NotBlank String version) {
		super();
		this.artist = artist;
		this.title = title;
		this.version = version;
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

	@Override
	public int hashCode() {
		return Objects.hash(artist, title, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongVersionRequest other = (SongVersionRequest) obj;
		return Objects.equals(artist, other.artist) && Objects.equals(title, other.title)
				&& Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "SongVersionRequest [artist=" + artist + ", title=" + title + ", version=" + version + "]";
	}

}
