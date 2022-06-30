package de.us.web.payload.request;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentSimpleRequest {

	@NotNull
	private Long id;
	@NotNull
	private String userName;
	@NotNull
	@Size(min=1, max=50)
	private String text;
	@NotNull
	private Timestamp date;
	
	public CommentSimpleRequest(Long id, String userName, String text, Timestamp date) {
		super();
		this.id = id;
		this.userName = userName;
		this.text = text;
		this.date = date;
	}

	public CommentSimpleRequest() {
		
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
		return Objects.hash(date, id, text, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentSimpleRequest other = (CommentSimpleRequest) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id) && Objects.equals(text, other.text)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "PageCommentSimple [id=" + id + ", userName=" + userName + ", text=" + text + ", date=" + date + "]";
	}
}
