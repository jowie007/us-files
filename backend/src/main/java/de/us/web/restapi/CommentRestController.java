package de.us.web.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.us.web.dto.CommentDto;
import de.us.web.payload.request.CommentSimpleRequest;
import de.us.web.payload.request.SimplePageRequest;
import de.us.web.services.CommentService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/comment")
public class CommentRestController {

	private CommentService commentService;

	@Autowired
	public CommentRestController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentDto createCommentForMainPage(@RequestBody CommentDto commentDto) {
		return commentService.saveCommentDto(commentDto);
	}
	
	@PostMapping(value = "/{songId}", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<CommentSimpleRequest> getCommentsForSongIdSimplyfied(@PathVariable Long songId,
			@RequestBody SimplePageRequest simplePageRequest) {
		return this.commentService.getCommentsSimplyfied(songId, simplePageRequest.getPage(), simplePageRequest.getSize());
	}

	@PostMapping(value = "/page", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<CommentSimpleRequest> getCommentsForMainPageSimplyfied(
			@RequestBody SimplePageRequest simplePageRequest) {
		return this.commentService.getCommentsSimplyfied(Long.valueOf(-1), simplePageRequest.getPage(), simplePageRequest.getSize());
	}
	
	@GetMapping(value = "/{songId}/count")
	@ResponseStatus(HttpStatus.OK)
	public int getCommentsForSongIdCount(@PathVariable Long songId) {
		return this.commentService.getCommentsCountById(songId);
	}
	
	@GetMapping(value = "/page/count")
	@ResponseStatus(HttpStatus.OK)
	public int getCommentsForMainPageCount() {
		return this.commentService.getCommentsCountById(Long.valueOf(-1));
	}

}
