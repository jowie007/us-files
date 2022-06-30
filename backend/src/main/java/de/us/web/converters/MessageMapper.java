package de.us.web.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.us.web.domain.Message;
import de.us.web.dto.MessageDto;
import de.us.web.payload.response.MessageResponse;
import de.us.web.services.SongService;

@Component
public class MessageMapper {

	private SongService songService;

	@Autowired
	public MessageMapper(SongService songService) {
		this.songService = songService;
	}

	public MessageDto messageToDto(Message message) {
		return new MessageDto(message.getId(), message.getSongId(), message.getUserName(), message.getDate(), message.getRead(),
				message.getDeleted(), message.getMessageType());
	}

	public Message dtoToMessage(MessageDto messageDto) {
		return new Message(messageDto.getSongId(), messageDto.getUserName(), messageDto.getDate(), messageDto.getRead(),
				messageDto.getDeleted(), messageDto.getMessageType());
	}

	public MessageResponse dtoToMessageResponse(MessageDto messageDto) {
		return new MessageResponse(messageDto, songService.getSongResponseById(messageDto.getSongId()));
	}

	public MessageResponse messageToMessageResponse(Message message) {
		return new MessageResponse(messageToDto(message), songService.getSongResponseById(message.getSongId()));
	}
}
