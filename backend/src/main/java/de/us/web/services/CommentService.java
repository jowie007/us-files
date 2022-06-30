package de.us.web.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.us.web.converters.CommentMapper;
import de.us.web.domain.Comment;
import de.us.web.dto.CommentDto;
import de.us.web.payload.request.CommentSimpleRequest;
import de.us.web.repositories.CommentRepository;

@Service
public class CommentService {

	private CommentRepository commentRepository;
	private CommentMapper commentMapper;
	private MessageService messageService;

	@Autowired
	public CommentService(CommentRepository commentRepository, CommentMapper commentMapper,
			MessageService messageService) {
		this.commentRepository = commentRepository;
		this.commentMapper = commentMapper;
		this.messageService = messageService;
	}

	public List<CommentDto> getAllCommentsById(Long songId) {
		List<CommentDto> commentDtos = new ArrayList<>();
		Iterable<Comment> commentEntities = commentRepository.findAllBySongIdOrderByDateDesc(songId);
		for (Comment comment : commentEntities) {
			commentDtos.add(commentMapper.commentToDto(comment));
		}
		return commentDtos;
	}

	public List<CommentDto> getCommentsById(Long songId, Integer page, Integer size) {
		List<CommentDto> commentDtos = new ArrayList<>();
		Iterable<Comment> commentEntities = commentRepository.findAllBySongIdOrderByDateDesc(songId,
				PageRequest.of(page, size));
		for (Comment comment : commentEntities) {
			commentDtos.add(commentMapper.commentToDto(comment));
		}
		return commentDtos;
	}

	public List<CommentSimpleRequest> getCommentsSimplyfied(Long songId, Integer page, Integer size) {
		List<CommentDto> commentDto = getCommentsById(songId, page, size);
		return commentDto.stream().map(e -> commentMapper.simplifyCommentDto(e)).collect(Collectors.toList());
	}

	public Integer getCommentsCountById(Long songId) {
		return commentRepository.findAllBySongIdOrderByDateDesc(songId).size();
	}

	public Comment saveComment(Comment comment) {
		if (comment.getDate() == null) {
			Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
			comment.setDate(timestamp);
		}
		messageService.saveNewCommentMessage(comment.getDate(), comment.getUser().getName(), comment.getSongId());
		return commentRepository.save(comment);
	}

	public CommentDto saveCommentDto(CommentDto commentDto) {
		Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		commentDto.setDate(timestamp);
		messageService.saveNewCommentMessage(timestamp, commentDto.getUserDto().getName(), commentDto.getSongId());
		Comment commentEntity = commentMapper.dtoToComment(commentDto);
		return commentMapper.commentToDto(commentRepository.save(commentEntity));
	}
}
