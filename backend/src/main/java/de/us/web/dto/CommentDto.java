package de.us.web.dto;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class CommentDto {

	@NotNull
	private Long id;
	@NotNull
	private Long songId;
	@NotNull
	private UserDto userDto;
	@NotNull
	private String text;
	private Timestamp date;

	public CommentDto(@NotNull Long songId, @NotNull UserDto userDto, @NotNull String text,
			Timestamp date) {
		super();
		this.songId = songId;
		this.userDto = userDto;
		this.text = text;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSongId() {
		return songId;
	}

	public void setSongId(Long songId) {
		this.songId = songId;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, songId, text, userDto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentDto other = (CommentDto) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id) && Objects.equals(songId, other.songId)
				&& Objects.equals(text, other.text) && Objects.equals(userDto, other.userDto);
	}

	@Override
	public String toString() {
		return "CommentDto [id=" + id + ", songId=" + songId + ", userDto=" + userDto + ", text=" + text + ", date="
				+ date + "]";
	}

}
