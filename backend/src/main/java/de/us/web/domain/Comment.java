package de.us.web.domain;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long songId;
	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;
	private String text;
	private Timestamp date;
	
	public Comment() {
		super();
	}

	public Comment(Long songId, User user, String text, Timestamp date) {
		super();
		this.user = user;
		this.text = text;
		this.date = date;
		this.songId = songId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Long getSongId() {
		return songId;
	}

	public void setSongId(Long songId) {
		this.songId = songId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, songId, text, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id) && Objects.equals(songId, other.songId)
				&& Objects.equals(text, other.text) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", user=" + user + ", text=" + text + ", date=" + date + ", songId=" + songId
				+ "]";
	}

}
