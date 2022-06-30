package de.us.web.restapi;

import java.util.ArrayList;
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
import de.us.web.dto.SongDto;
import de.us.web.payload.request.SearchRequest;
import de.us.web.payload.request.SongRequest;
import de.us.web.payload.request.UserSongRelationRequest;
import de.us.web.payload.response.SongResponse;
import de.us.web.payload.response.UserSongRelationResponse;
import de.us.web.services.MessageService;
import de.us.web.services.SongService;
import de.us.web.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/song", produces = "application/json")
public class SongRestController {

	private final SongService songService;
	private final StorageService storageService;
	private final MessageService messageService;

	@Autowired
	public SongRestController(SongService songService, StorageService storageService, MessageService messageService) {
		this.songService = songService;
		this.storageService = storageService;
		this.messageService = messageService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SongDto createSong(@RequestBody SongDto songDto) {
		return songService.saveSong(songDto);
	}

	@GetMapping(value = "/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Long> getAllIds() {
		List<Long> ids = new ArrayList<>();
		for (SongDto songDto : songService.getAllSongs()) {
			ids.add(songDto.getId());
		}
		return ids;
	}

	@GetMapping(value = "/top")
	@ResponseStatus(HttpStatus.OK)
	public List<Long> getTopIds() {
		List<Long> ids = new ArrayList<>();
		for (SongDto songDto : songService.getMostDownloadedSongs(7, 50)) {
			ids.add(songDto.getId());
		}
		return ids;
	}

	@GetMapping(value = "/newest")
	@ResponseStatus(HttpStatus.OK)
	public List<Long> getNewIds() {
		List<Long> ids = new ArrayList<>();
		for (SongDto songDto : songService.getNewestSongs(50)) {
			ids.add(songDto.getId());
		}
		return ids;
	}

	@PostMapping(value = "/save", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Long saveSong(@RequestBody SongRequest songRequest) {
		return songService.saveSongRequest(songRequest);
	}

	@PostMapping(value = "/version/exists", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public boolean checkIfVersionExists(@RequestBody SongRequest songRequest) {
		return songService.checkIfSongVersionExists(songRequest.getArtist(), songRequest.getTitle(),
				songRequest.getVersion());
	}

	@PostMapping(value = "/search/")
	@ResponseStatus(HttpStatus.OK)
	public List<UserSongRelationResponse> getSearchedSongs(@RequestBody SearchRequest searchRequest) {
		return songService.getUserSongRelationResponsesBySearchRequest(searchRequest, searchRequest.getUserName());
	}

	@PostMapping(value = "/search/count")
	@ResponseStatus(HttpStatus.OK)
	public int getSearchedSongsCount(@RequestBody SearchRequest searchRequest) {
		return songService.getSongDtosBySearchAndOrder(searchRequest, null, null).size();
	}

	@GetMapping(value = "/randomid")
	@ResponseStatus(HttpStatus.OK)
	public Long getRandomSongId() {
		return songService.getRandomSongId();
	}

	@GetMapping(value = "/{id}/versions")
	@ResponseStatus(HttpStatus.OK)
	public List<UserSongRelationResponse> getAllVersions(@PathVariable Long id) {
		return songService.getUserSongRelationResponseVersionsByUserNameAndSongId("", id);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public SongResponse getSongResponseById(@PathVariable Long id) {
		return songService.getSongResponseById(id);
	}

	@GetMapping(value = "/{id}/editor/add/{editorName}")
	@ResponseStatus(HttpStatus.OK)
	public boolean addEditorToSong(@PathVariable Long id, @PathVariable String editorName) {
		return songService.addEditorToSong(id, editorName);
	}

	@GetMapping(value = "/{songId}/{userName}/versions")
	@ResponseStatus(HttpStatus.OK)
	public List<UserSongRelationResponse> getUserSongRelationResponseVersionsBySongIdAndUserName(
			@PathVariable Long songId, @PathVariable String userName) {
		return songService.getUserSongRelationResponseVersionsByUserNameAndSongId(userName, songId);
	}

	@GetMapping(value = "/{songId}/updated")
	@ResponseStatus(HttpStatus.OK)
	public void markSongAsUpdated(@PathVariable Long songId) {
		songService.markSongAsUpdatedById(songId);
		messageService.saveSongUpdatedMessage(songId);
	}

	@GetMapping(value = "/{songId}/{userName}")
	@ResponseStatus(HttpStatus.OK)
	public UserSongRelationResponse getUserSongRelationResponseBySongIdAndUserName(@PathVariable Long songId,
			@PathVariable String userName) {
		return songService.getUserSongRelationResponseByUserNameAndSongId(userName, songId);
	}

	@PostMapping(value = "/{songId}/{userName}")
	@ResponseStatus(HttpStatus.OK)
	public void setUserSongRelation(@RequestBody UserSongRelationRequest userSongRelationRequest) {
		songService.saveUserSongRelationRequest(userSongRelationRequest);
	}

	@GetMapping(value = "/calendar/{year}/{month}")
	@ResponseStatus(HttpStatus.OK)
	public Map<Integer, List<SongResponse>> getSongsByYearAndMonth(@PathVariable int year, @PathVariable int month) {
		return songService.getSongsByYearAndMonth(year, month);
	}

	// https://spring.io/guides/gs/uploading-files/
	@PostMapping(value = "/save/{id}/cover")
	@ResponseStatus(HttpStatus.OK)
	public boolean handleCoverUpload(@RequestParam("file") MultipartFile file, @PathVariable String id) {
		storageService.setSubFolder("cover");
		storageService.delete(id);
		storageService.store(file, id);
		return true;
	}

	@GetMapping("/{id}/cover")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
		storageService.setSubFolder("cover");
		Resource file = songService.loadCoverFileById(id);
		return file == null ? null
				: ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
						.body(file);
	}
}
