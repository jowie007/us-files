package de.us.web.payload.response;

import java.util.Objects;

import de.us.web.dto.MessageDto;

public class MessageResponse {

	private MessageDto messageDto;
	private SongResponse songResponse;

	public MessageResponse(MessageDto messageDto, SongResponse songResponse) {
		super();
		this.messageDto = messageDto;
		this.songResponse = songResponse;
	}

	public MessageDto getMessageDto() {
		return messageDto;
	}

	public void setMessageDto(MessageDto messageDto) {
		this.messageDto = messageDto;
	}

	public SongResponse getSongResponse() {
		return songResponse;
	}

	public void setSongResponse(SongResponse songResponse) {
		this.songResponse = songResponse;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageDto, songResponse);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageResponse other = (MessageResponse) obj;
		return Objects.equals(messageDto, other.messageDto) && Objects.equals(songResponse, other.songResponse);
	}

	@Override
	public String toString() {
		return "MessageResponse [messageDto=" + messageDto + ", songResponse=" + songResponse + "]";
	}

}
