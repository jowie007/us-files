package de.us.web.domain;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private String name;
	private String email;
	private String password;
	private String salt;
	private Timestamp registrationDate;
	private RoleEnum role;

	public User() {

	}

	public User(String name, String email, String password, String salt, Timestamp registrationDate, RoleEnum role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.registrationDate = registrationDate;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, name, password, registrationDate, role, salt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(registrationDate, other.registrationDate)
				&& role == other.role && Objects.equals(salt, other.salt);
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", password=" + password + ", salt=" + salt
				+ ", registrationDate=" + registrationDate + ", role=" + role + "]";
	}

}
