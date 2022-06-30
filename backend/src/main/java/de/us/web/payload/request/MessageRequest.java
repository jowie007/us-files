package de.us.web.payload.request;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;

public class MessageRequest {

	@NotEmpty
	private List<Long> messageIds;
	private Boolean read;
	private Boolean deleted;

	public MessageRequest(@NotEmpty List<Long> messageIds, Boolean read, Boolean deleted) {
		super();
		this.messageIds = messageIds;
		this.read = read;
		this.deleted = deleted;
	}

	public List<Long> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<Long> messageIds) {
		this.messageIds = messageIds;
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

	@Override
	public int hashCode() {
		return Objects.hash(deleted, messageIds, read);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageRequest other = (MessageRequest) obj;
		return Objects.equals(deleted, other.deleted) && Objects.equals(messageIds, other.messageIds)
				&& Objects.equals(read, other.read);
	}

	@Override
	public String toString() {
		return "MessageRequest [messageIds=" + messageIds + ", read=" + read + ", deleted=" + deleted + "]";
	}

}
