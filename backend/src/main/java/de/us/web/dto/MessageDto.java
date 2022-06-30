package de.us.web.dto;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import de.us.web.domain.MessageTypeEnum;

public class MessageDto {

	@NotNull
	private Long id;
	@NotNull
	private Long songId;
	@NotNull
	private String userName;
	@NotNull
	private Timestamp date;
	@NotNull
	private Boolean read;
	@NotNull
	private Boolean deleted;
	@NotNull
	private MessageTypeEnum messageType;

	public MessageDto(Long id, Long songId, String userName, Timestamp date, Boolean read, Boolean deleted,
			MessageTypeEnum messageType) {
		super();
		this.id = id;
		this.songId = songId;
		this.userName = userName;
		this.date = date;
		this.read = read;
		this.deleted = deleted;
		this.messageType = messageType;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public MessageTypeEnum getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageTypeEnum messageType) {
		this.messageType = messageType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, deleted, id, messageType, read, songId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageDto other = (MessageDto) obj;
		return Objects.equals(date, other.date) && Objects.equals(deleted, other.deleted)
				&& Objects.equals(id, other.id) && messageType == other.messageType && Objects.equals(read, other.read)
				&& Objects.equals(songId, other.songId) && Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "MessageDto [id=" + id + ", songId=" + songId + ", userName=" + userName + ", date=" + date + ", read="
				+ read + ", deleted=" + deleted + ", messageType=" + messageType + "]";
	}

}
