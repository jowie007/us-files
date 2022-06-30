package de.us.web.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.us.web.converters.MessageMapper;
import de.us.web.domain.Message;
import de.us.web.domain.MessageTypeEnum;
import de.us.web.domain.Song;
import de.us.web.domain.User;
import de.us.web.payload.request.MessageRequest;
import de.us.web.payload.response.MessageResponse;
import de.us.web.repositories.MessageRepository;

// https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
@Transactional
@Service
public class MessageService {

	private MessageRepository messageRepository;
	private SongService songService;
	private MessageMapper messageMapper;

	@Autowired
	public MessageService(MessageRepository messageRepository, SongService songService, MessageMapper messageMapper) {
		this.messageRepository = messageRepository;
		this.songService = songService;
		this.messageMapper = messageMapper;
	}

	public void saveNewCommentMessage(Timestamp timestamp, String authorName, Long songId) {
		Song song = songService.getSongById(songId);
		if (!song.getCreator().getName().equals(authorName)) {
			messageRepository.save(new Message(songId, song.getCreator().getName(), timestamp, false, false,
					MessageTypeEnum.NEW_COMMENT));
		}
		for (User editor : song.getEditors()) {
			if (!editor.getName().equals(authorName)) {
				messageRepository.save(
						new Message(songId, editor.getName(), timestamp, false, false, MessageTypeEnum.NEW_COMMENT));
			}
		}
	}

	public void saveSongUpdatedMessage(Long songId) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		for (User user : songService.findAllUsersWhoFavoritedBySongId(songId)) {
			messageRepository
					.save(new Message(songId, user.getName(), timestamp, false, false, MessageTypeEnum.SONG_UPDATED));
		}
	}

	public List<Message> getMessagesByUserName(String userName) {
		return messageRepository.findAllByUserNameAndDeletedOrderByDateDesc(userName, false);
	}

	public List<MessageResponse> getMessageResponsesByUserName(String userName) {
		List<MessageResponse> messageResponses = new ArrayList<>();
		for (Message message : getMessagesByUserName(userName)) {
			messageResponses.add(messageMapper.messageToMessageResponse(message));
		}
		return messageResponses;
	}

	public boolean saveMessagesStatus(MessageRequest messageRequest) {
		for (Long messageId : messageRequest.getMessageIds()) {
			Message message = messageRepository.findById(messageId).get();
			if (messageRequest.getRead() != null) {
				message.setRead(messageRequest.getRead());
			}
			if (messageRequest.getDeleted() != null) {
				message.setDeleted(messageRequest.getDeleted());
			}
			messageRepository.save(message);
		}
		return false;
	}

	public boolean getNewMessagesStatus(String userName) {
		return messageRepository.findAllByUserNameAndDeletedAndRead(userName, false, false).size() > 0 ? true : false;
	}
}
