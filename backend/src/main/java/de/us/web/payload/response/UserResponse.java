package de.us.web.payload.response;

import java.sql.Timestamp;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import de.us.web.domain.RoleEnum;

public class UserResponse {

	@NotNull
	private String name;
	@NotNull
	private Timestamp registrationDate;
	@NotNull
	private RoleEnum role;
	private int createdSongs;
	private int editedSongs;

	public UserResponse(@NotNull String name, @NotNull Timestamp registrationDate, @NotNull RoleEnum role,
			int createdSongs, int editedSongs) {
		super();
		this.name = name;
		this.registrationDate = registrationDate;
		this.role = role;
		this.createdSongs = createdSongs;
		this.editedSongs = editedSongs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public int getCreatedSongs() {
		return createdSongs;
	}

	public void setCreatedSongs(int createdSongs) {
		this.createdSongs = createdSongs;
	}

	public int getEditedSongs() {
		return editedSongs;
	}

	public void setEditedSongs(int editedSongs) {
		this.editedSongs = editedSongs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdSongs, editedSongs, name, registrationDate, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserResponse other = (UserResponse) obj;
		return createdSongs == other.createdSongs && editedSongs == other.editedSongs
				&& Objects.equals(name, other.name) && Objects.equals(registrationDate, other.registrationDate)
				&& role == other.role;
	}

	@Override
	public String toString() {
		return "UserResponse [name=" + name + ", registrationDate=" + registrationDate + ", role=" + role
				+ ", createdSongs=" + createdSongs + ", editedSongs=" + editedSongs + "]";
	}

}
