package de.us.web.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.us.web.domain.Comment;
import de.us.web.dto.CommentDto;
import de.us.web.payload.request.CommentSimpleRequest;

@Component
public class CommentMapper {

	private UserMapper userMapper;

	@Autowired
	public CommentMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public CommentDto commentToDto(Comment comment) {
		return comment == null ? null
				: new CommentDto(comment.getSongId(), userMapper.userToDto(comment.getUser()), comment.getText(),
						comment.getDate());
	}

	public Comment dtoToComment(CommentDto commentDto) {
		return commentDto == null ? null
				: new Comment(commentDto.getSongId(), userMapper.dtoToUser(commentDto.getUserDto()),
						commentDto.getText(), commentDto.getDate());
	}

	public CommentSimpleRequest simplifyCommentDto(CommentDto commentDto) {
		return commentDto == null ? null
				: new CommentSimpleRequest(commentDto.getId(), commentDto.getUserDto().getName(), commentDto.getText(),
						commentDto.getDate());
	}
}
