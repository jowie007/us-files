package de.us.web.services;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.us.web.converters.SongMapper;
import de.us.web.converters.UserSongRelationMapper;
import de.us.web.domain.Song;
import de.us.web.domain.User;
import de.us.web.domain.UserSongRelation;
import de.us.web.dto.SongDto;
import de.us.web.payload.request.SearchParamsRequest;
import de.us.web.payload.request.SearchRequest;
import de.us.web.payload.request.SongRequest;
import de.us.web.payload.request.UserSongRelationRequest;
import de.us.web.payload.response.SongResponse;
import de.us.web.payload.response.UserSongRelationResponse;
import de.us.web.repositories.SongRepository;
import de.us.web.repositories.UserRepository;
import de.us.web.repositories.UserSongRelationRepository;
import de.us.web.storage.StorageFileNotFoundException;
import de.us.web.storage.StorageService;

@Service
public class SongService {

	private SongRepository songRepository;
	private SongMapper songMapper;
	private UserSongRelationMapper userSongRelationMapper;
	private StorageService storageService;
	private UserService userService;
	private UserSongRelationRepository userSongRelationRepository;
	private UserRepository userRepository;

	@Autowired
	public SongService(SongRepository songRepository, SongMapper songMapper,
			UserSongRelationMapper userSongRelationMapper, StorageService storageService, UserService userService,
			UserSongRelationRepository userSongRelationRepository, UserRepository userRepository) {
		this.songRepository = songRepository;
		this.songMapper = songMapper;
		this.userSongRelationMapper = userSongRelationMapper;
		this.storageService = storageService;
		this.userService = userService;
		this.userSongRelationRepository = userSongRelationRepository;
		this.userRepository = userRepository;
	}

	public List<SongDto> getAllSongs() {
		List<SongDto> songDtos = new ArrayList<>();
		Iterable<Song> songEntities = songRepository.findAll();
		for (Song song : songEntities) {
			songDtos.add(songMapper.songToDto(song));
		}
		return songDtos;
	}

	public List<SongDto> getMostDownloadedSongs(int daycount, int songcount) {
		List<SongDto> songDtos = new ArrayList<>();
		Iterable<Song> songEntities = songRepository.findAllByFinishedDateIsNotNull();
		Map<SongDto, Integer> songDtoWithDownloadsMap = new LinkedHashMap<>();
		for (Song song : songEntities) {
			songDtoWithDownloadsMap.put(songMapper.songToDto(song), getDownloadCountForSong(song, daycount));
		}
		// https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
		songDtoWithDownloadsMap = songDtoWithDownloadsMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(songcount)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		for (SongDto songDto : songDtoWithDownloadsMap.keySet()) {
			songDtos.add(songDto);
		}
		return songDtos;
	}

	public List<SongDto> getNewestSongs(int songcount) {
		List<SongDto> songDtos = new ArrayList<>();
		// https://stackoverflow.com/questions/44565820/what-is-the-limit-clause-alternative-in-jpql
		Iterable<Song> songEntities = songRepository
				.findByFinishedDateIsNotNullOrderByFinishedDateDescTitleAscArtistAscVersionAsc(
						PageRequest.of(0, songcount));
		for (Song song : songEntities) {
			songDtos.add(songMapper.songToDto(song));
		}
		return songDtos;
	}

	public int getDownloadCountForSong(Song song, int daycount) {
		Timestamp startTime = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * daycount));
		int count = 0;
		for (UserSongRelation userSongRelation : userSongRelationRepository.findBySong(song)) {
			if (userSongRelation.getDownloadDate() != null && userSongRelation.getDownloadDate().after(startTime)) {
				count++;
			}
		}
		return count;
	}

	public SongDto getSongDtoById(Long id) {
		if (songRepository.findById(id).isPresent()) {
			Song songEntity = songRepository.findById(id).get();
			return songMapper.songToDto(songEntity);
		}
		throw new NullPointerException("Song with ID " + id + " not found.");
	}

	public UserSongRelationResponse getUserSongRelationResponseByUserNameAndSongId(String userName, Long songId) {
		if (userName != "") {
			Optional<UserSongRelation> userSongRelation = userSongRelationRepository
					.findByUserAndSong(userService.getUserByName(userName), getSongById(songId));
			if (userSongRelation.isPresent()) {
				UserSongRelation userSongRelationEntity = userSongRelation.get();
				SongResponse songResponse = songMapper.songDtoToSongResponse(getSongDtoById(songId));
				songResponse.setAverageRating(getAverageRatingBySongId(songId));
				songResponse.setDownloads(getDownloadsBySongId(songId));
				return new UserSongRelationResponse(
						userSongRelationMapper.userSongRelationToDto(userSongRelationEntity), songResponse);
			}
		}
		return new UserSongRelationResponse(userName, getSongResponseById(songId));
	}

	public void saveUserSongRelationRequest(UserSongRelationRequest userSongRelationRequest) {
		User user = userService.getUserByName(userSongRelationRequest.getUserName());
		Song song = getSongById(userSongRelationRequest.getSongId());
		Optional<UserSongRelation> userSongRelation = userSongRelationRepository.findByUserAndSong(user, song);
		UserSongRelation userSongRelationToSave = null;
		if (userSongRelation.isPresent()) {
			userSongRelationToSave = userSongRelation.get();
		} else {
			userSongRelationToSave = new UserSongRelation(user, song);
		}
		if (userSongRelationRequest.getDownloaded() == true) {
			userSongRelationToSave.setDownloadDate(new java.sql.Timestamp(System.currentTimeMillis()));
		}
		// Wenn der Song neu favorisiert wurde
		if (userSongRelationRequest.getFavorite() != userSongRelationToSave.isFavorite()) {
			if (userSongRelationRequest.getFavorite()) {
				userSongRelationToSave.setFavoriteDate(new java.sql.Timestamp(System.currentTimeMillis()));
			} else {
				userSongRelationToSave.setFavoriteDate(null);
			}
			userSongRelationToSave.setFavorite(userSongRelationRequest.getFavorite());
		}
		// Wenn sich das Rating geändert hat
		if (userSongRelationRequest.getRating() != userSongRelationToSave.getRating()) {
			userSongRelationToSave.setRatingDate(new java.sql.Timestamp(System.currentTimeMillis()));
			userSongRelationToSave.setRating(userSongRelationRequest.getRating());
		}
		userSongRelationRepository.save(userSongRelationToSave);

	}

	public Song getSongById(Long id) {
		if (songRepository.findById(id).isPresent()) {
			return songRepository.findById(id).get();
		}
		throw new NullPointerException("Song with ID " + id + " not found");
	}

	public List<Integer> getRatingsBySongId(Long id) {
		List<Integer> ratings = new ArrayList<>();
		for (UserSongRelation userSongRelation : userSongRelationRepository.findBySong(getSongById(id))) {
			if (userSongRelation.getRating() > 0) {
				ratings.add(userSongRelation.getRating());
			}
		}
		return ratings;
	}

	public double getAverageRatingBySongId(Long id) {
		List<Integer> ratings = getRatingsBySongId(id);
		double averageRating = 0.0;
		for (Integer rating : getRatingsBySongId(id)) {
			averageRating += ((double) rating) / ratings.size();
		}
		return averageRating;
	}

	public int getDownloadsBySongId(Long id) {
		int downloads = 0;
		for (UserSongRelation userSongRelation : userSongRelationRepository.findBySong(getSongById(id))) {
			if (userSongRelation.getDownloadDate() != null) {
				downloads++;
			}
		}
		return downloads;
	}

	public List<UserSongRelationResponse> getUserSongRelationResponsesBySearchRequest(SearchRequest searchRequest,
			String userName) {
		boolean sortAverageRating = false;
		boolean sortDownloads = false;
		// Notwendig, um beim Sortieren nach Bewertung die Richtung beizubehalten
		String oldDirection = searchRequest.getSortDirection();
		if (searchRequest.getSortColumn().trim().equals("AverageRating")) {
			searchRequest.setSortColumn("Artist");
			searchRequest.setSortDirection("Asc");
			sortAverageRating = true;
		} else if (searchRequest.getSortColumn().trim().equals("Downloads")) {
			searchRequest.setSortColumn("Artist");
			searchRequest.setSortDirection("Asc");
			sortDownloads = true;
		}
		List<SongDto> songDtos = this.getSongDtosBySearchAndOrder(searchRequest, searchRequest.getSortColumn(),
				searchRequest.getSortDirection());
		if (sortAverageRating) {
			// https://stackoverflow.com/questions/4018090/sorting-listclass-by-one-of-its-variable
			Collections.sort(songDtos, new Comparator<SongDto>() {
				@Override
				public int compare(SongDto o1, SongDto o2) {
					double ratingO1 = getAverageRatingBySongId(o1.getId());
					double ratingO2 = getAverageRatingBySongId(o2.getId());
					int ret = 0;
					if (ratingO1 > ratingO2) {
						ret = -1;
					} else if (ratingO1 < ratingO2) {
						ret = 1;
					}
					return oldDirection.trim().equals("Asc") ? ret : -ret;
				}
			});
			System.out.println("sortAverageRating");
		} else if (sortDownloads) {
			// https://stackoverflow.com/questions/4018090/sorting-listclass-by-one-of-its-variable
			Collections.sort(songDtos, new Comparator<SongDto>() {
				@Override
				public int compare(SongDto o1, SongDto o2) {
					int downloadsO1 = getDownloadsBySongId(o1.getId());
					int downloadsO2 = getDownloadsBySongId(o2.getId());
					int ret = 0;
					if (downloadsO1 > downloadsO2) {
						ret = -1;
					} else if (downloadsO1 < downloadsO2) {
						ret = 1;
					}
					return oldDirection.trim().equals("Asc") ? ret : -ret;
				}
			});
		}
		int page = searchRequest.getPage();
		int count = searchRequest.getCount();
		int currentStart = (page - 1) * count;
		int currentEnd = currentStart + count >= songDtos.size() ? songDtos.size() : currentStart + count;
		songDtos = songDtos.subList(currentStart, currentEnd);
		List<UserSongRelationResponse> userSongRelationResponses = new ArrayList<>();
		for (SongDto songDto : songDtos) {
			userSongRelationResponses.add(getUserSongRelationResponseByUserNameAndSongId(userName, songDto.getId()));
		}
		return userSongRelationResponses;
	}

	@SuppressWarnings("unchecked")
	public List<SongDto> getSongDtosBySearchAndOrder(SearchRequest searchRequest, String sortColumn,
			String sortDirection) {
		List<SongDto> songDtos = new ArrayList<>();
		Iterable<Song> songEntities = null;
		if (sortColumn == null || sortDirection == null) {
			songEntities = songRepository.findAll();
		} else {
			// https://stackoverflow.com/questions/160970/how-do-i-invoke-a-java-method-when-given-the-method-name-as-a-string
			Method method = null;
			try {
				// https://stackoverflow.com/questions/3023354/how-to-get-string-name-of-a-method-in-java
				for (Method declaredMethod : songRepository.getClass().getDeclaredMethods()) {
					if (declaredMethod.getName().contains("findAllByOrderBy" + sortColumn + sortDirection)) {
						method = declaredMethod;
					}
				}
				// method = songRepository.getClass().getMethod("findAllByOrderBy" + sortColumn
				// + sortDirection);
				if (method != null) {
					songEntities = (Iterable<Song>) method.invoke(songRepository);
				}
			} catch (Exception e) {
				System.out.println("findAllByOrderBy" + sortColumn + sortDirection);
				System.out.println("Method not found");
				songEntities = songRepository.findAll();
			}
		}
		if (songEntities != null) {
			// Wenn keine Search-Params übergeben wurden
			if (searchRequest.getSearchParams() == null) {
				for (Song song : songEntities) {
					boolean containsAllParts = true;
					boolean isNotFiltered = true;
					for (String searchPart : searchRequest.getSearchString().toLowerCase().trim().split(" ")) {
						if (containsAllParts && !(song.getArtist().trim().toLowerCase().contains(searchPart)
								|| song.getTitle().trim().toLowerCase().contains(searchPart)
								|| song.getVersion().trim().toLowerCase().contains(searchPart))) {
							containsAllParts = false;
						}
						if (searchRequest.isFilterFavorites()
								&& !getUserSongRelationResponseByUserNameAndSongId(searchRequest.getUserName(),
										song.getId()).getFavorite()) {
							isNotFiltered = false;
						}
						if (!searchRequest.isFilterUnfinished() && song.getPercentage() < 100) {
							isNotFiltered = false;
						}
					}
					if (containsAllParts && isNotFiltered) {
						songDtos.add(songMapper.songToDto(song));
					}
				}
			}
			// Sonst
			else {
				for (Song song : songEntities) {
					boolean add = true;
					if (!(!searchRequest.isFilterUnfinished() && song.getPercentage() < 100)) {
						SearchParamsRequest spm = searchRequest.getSearchParams();
						if (spm.getArtist() != null && spm.getArtist() != ""
								&& !song.getArtist().toLowerCase().contains(spm.getArtist().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getTitle() != null && spm.getTitle() != ""
								&& !song.getTitle().toLowerCase().contains(spm.getTitle().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getVersion() != null && spm.getVersion() != ""
								&& !song.getVersion().toLowerCase().contains(spm.getVersion().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getGenre() != null && spm.getGenre() != ""
								&& !song.getGenre().toLowerCase().contains(spm.getGenre().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getLanguage() != null && spm.getLanguage() != ""
								&& !song.getLanguage().toLowerCase().contains(spm.getLanguage().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getReleaseYear() != null && !song.getReleaseYear().equals(spm.getReleaseYear())) {
							add = false;
						}
						if (spm.getCreator() != null && spm.getCreator() != "" && !song.getCreator().getName()
								.toLowerCase().trim().equals(spm.getCreator().toLowerCase().trim())) {
							add = false;
						}
						if (spm.getEditor() != null && spm.getEditor() != "") {
							boolean containsEditor = false;
							for (User editor : song.getEditors()) {
								if (editor.getName().toLowerCase().trim()
										.equals(spm.getEditor().toLowerCase().trim())) {
									containsEditor = true;
								}
							}
							if (!containsEditor) {
								add = false;
							}
						}
						if (add) {
							songDtos.add(songMapper.songToDto(song));
						}
					}
				}
			}
		}
		return songDtos;
	}

	public Long saveSongRequest(SongRequest songRequest) {
		SongDto songDto = songMapper.songRequestToSongDto(songRequest);
		Song song = null;
		if (songRequest.getId() != null) {
			song = songMapper.getSongToUpdate(songRequest);
		} else {
			song = songMapper.dtoToSong(songDto);
		}
		return songRepository.save(song).getId();
	}

	public SongDto saveSong(SongDto songDto) {
		Song songEntity = songMapper.dtoToSong(songDto);
		return songMapper.songToDto(songRepository.save(songEntity));
	}

	public List<SongDto> getVersionsBySongDto(SongDto songDto) {
		List<Song> songs = new ArrayList<>();
		Song songEntity = songMapper.dtoToSong(songDto);
		if (songEntity.getParentSong() != null) {
			songEntity = songEntity.getParentSong();
		}
		songs.add(songEntity);
		songRepository.findByParentSong(songEntity).forEach(e -> songs.add(e));
		List<SongDto> songDtos = new ArrayList<>();
		songs.stream().forEach(e -> songDtos.add(songMapper.songToDto(e)));
		return songDtos;
	}

	public boolean checkIfSongVersionExists(String artist, String title, String version) {
		return songRepository.findAllByArtistAndTitleAndVersionIgnoreCase(artist, title, version).size() > 0 ? true
				: false;
	}

	public List<UserSongRelationResponse> getUserSongRelationResponseVersionsByUserNameAndSongId(String userName,
			Long songId) {
		List<Song> songs = new ArrayList<>();
		Song songEntity = songMapper.dtoToSong(getSongDtoById(songId));
		if (songEntity.getParentSong() != null) {
			songEntity = songEntity.getParentSong();
		}
		songs.add(songEntity);
		songRepository.findByParentSong(songEntity).forEach(e -> songs.add(e));
		List<UserSongRelationResponse> userSongRelationResponses = new ArrayList<>();
		songs.stream().forEach(e -> {
			userSongRelationResponses.add(getUserSongRelationResponseByUserNameAndSongId(userName, e.getId()));
		});
		return userSongRelationResponses;
	}

	public Long getRandomSongId() {
		List<Song> songs = new ArrayList<>();
		songRepository.findAll().forEach(e -> songs.add(e));
		Collections.shuffle(songs);
		return songs.get(0).getId();
	}

	public SongResponse getSongResponseById(Long id) {
		SongDto songDto = getSongDtoById(id);
		SongResponse songResponse = songMapper.songDtoToSongResponse(songDto);
		songResponse.setAverageRating(getAverageRatingBySongId(id));
		songResponse.setDownloads(getDownloadsBySongId(id));
		return songResponse;
	}

	public boolean addEditorToSong(Long sondId, String editorName) {
		boolean ret = true;
		User user = null;
		try {
			user = userRepository.findByName(editorName).get();
		} catch (NoSuchElementException e) {
			ret = false;
		}
		if (ret) {
			Song song = songRepository.findById(sondId).get();
			Set<User> editors = song.getEditors();
			editors.add(user);
			// song.setEditors(editors);
			songRepository.save(song);
		}
		return ret;
	}

	public Resource loadCoverFileById(Long id) {
		storageService.loadAll();
		Resource file = null;
		for (String fileExtension : new String[] { "jpg", "jpeg", "png" }) {
			try {
				file = storageService.loadAsResource(id + "." + fileExtension);
			} catch (StorageFileNotFoundException e) {

			}
		}
		return file;
	}

	public Map<Integer, List<SongResponse>> getSongsByYearAndMonth(int year, int month) {
		Timestamp firstDayOfMonth = Timestamp.valueOf(year + "-" + month + "-01 0:0:0.0");
		// https://stackoverflow.com/questions/9397203/last-day-of-month-calculation
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 0);
		int lastdayofmonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Timestamp lastDayOfMonth = Timestamp.valueOf(year + "-" + month + "-" + lastdayofmonth + " 23:59:59.999");
		List<Song> songs = songRepository
				.findAllByFinishedDateBetweenOrderByArtistAscTitleAscVersionAsc(firstDayOfMonth, lastDayOfMonth);
		Map<Integer, List<SongResponse>> ret = new HashMap<>();
		for (Song song : songs) {
			SongDto songDto = songMapper.songToDto(song);
			// https://stackoverflow.com/questions/6262570/how-to-retrieve-day-month-and-year-from-timestamplong-format
			calendar.setTime(songDto.getFinishedDate());
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			List<SongResponse> songsForDay = ret.get(day);
			if (songsForDay == null) {
				songsForDay = new ArrayList<>();
			}
			songsForDay.add(songMapper.songDtoToSongResponse(songDto));
			ret.put(day, songsForDay);
		}
		return ret;
	}

	public int getEditorSongCount(User user) {
		Iterable<Song> songs = songRepository.findAll();
		int count = 0;
		for (Song song : songs) {
			if (song.getEditors().contains(user)) {
				count++;
			}
		}
		return count;
	}

	public List<User> findAllUsersWhoFavoritedBySongId(Long songId) {
		List<User> users = new ArrayList<>();
		for (UserSongRelation userSongRelation : userSongRelationRepository.findBySong(getSongById(songId))) {
			if (userSongRelation.isFavorite()) {
				users.add(userSongRelation.getUser());
			}
		}
		return users;
	}

	public void markSongAsUpdatedById(Long songId) {
		Song song = getSongById(songId);
		song.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		songRepository.save(song);
	}
}
