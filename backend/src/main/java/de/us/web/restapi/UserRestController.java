package de.us.web.restapi;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.us.web.dto.UserDto;
import de.us.web.payload.request.MessageRequest;
import de.us.web.payload.response.MessageResponse;
import de.us.web.payload.response.UserResponse;
import de.us.web.services.MessageService;
import de.us.web.services.UserService;
import de.us.web.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user", produces = "application/json")
public class UserRestController {

	private UserService userService;
	private StorageService storageService;
	private MessageService messageService;

	@Autowired
	public UserRestController(UserService userService, StorageService storageService, MessageService messageService) {
		this.userService = userService;
		this.storageService = storageService;
		this.messageService = messageService;
	}

	@GetMapping(value = "/{name}")
	@ResponseStatus(HttpStatus.OK)
	public UserDto getUserByName(@PathVariable String name) {
		return userService.getUserDtoByName(name);
	}

	@GetMapping(value = "/{name}/info")
	@ResponseStatus(HttpStatus.OK)
	public UserResponse getUserResponseByName(@PathVariable String name) {
		return userService.getUserResponseByName(name);
	}

	@GetMapping(value = "/{name}/salt")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> getUserSaltByName(@PathVariable String name) {
		boolean userExists = userService.existUserByName(name);
		return userExists ? Collections.singletonMap("salt", userService.getUserDtoByName(name).getSalt()) : null;
	}

	@PostMapping(value = "/{name}/messages")
	@ResponseStatus(HttpStatus.OK)
	public boolean saveMessagesStatus(@RequestBody MessageRequest messageRequest,
			@PathVariable String name) {
		return messageService.saveMessagesStatus(messageRequest);
	}

	@GetMapping(value = "/{name}/messages")
	@ResponseStatus(HttpStatus.OK)
	public List<MessageResponse> getMessages(@PathVariable String name) {
		return messageService.getMessageResponsesByUserName(name);
	}
	
	@GetMapping(value = "/{name}/newMessages")
	@ResponseStatus(HttpStatus.OK)
	public boolean getNewMessagesStatus(@PathVariable String name) {
		return messageService.getNewMessagesStatus(name);
	}

	@GetMapping(value = "/delete/{name}/image")
	@ResponseStatus(HttpStatus.OK)
	public boolean handleProfileImageDelete(@PathVariable String name) {
		storageService.setSubFolder("profile");
		storageService.delete(name);
		return true;
	}

	// https://spring.io/guides/gs/uploading-files/
	@PostMapping(value = "/save/{name}/image")
	@ResponseStatus(HttpStatus.OK)
	public boolean handleProfileImageUpload(@RequestParam("file") MultipartFile file, @PathVariable String name) {
		storageService.setSubFolder("profile");
		storageService.store(file, name);
		return true;
	}

	@GetMapping("/{name}/image")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String name) {
		storageService.setSubFolder("profile");
		Resource file = userService.loadProfileFileByName(name);
		return file == null ? null
				: ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
						.body(file);
	}
}
