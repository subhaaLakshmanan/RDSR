package com.example.dsr.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email") })
public class User {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;

	@NotBlank
	@Size(max = 300)
	@Email
	private String email;

	@NotBlank
	@Size(max = 250)
	private String password;
	
	private String rawpassword;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getRawpassword() {
		return rawpassword;
	}

	public void setRawpassword(String rawpassword) {
		this.rawpassword = rawpassword;
	}

	public User(@NotBlank @Size(max = 300) @Email String email,@NotBlank @Size(max = 250) String password, String rawpassword) {
		super();
		this.email = email;
		this.password = password;
		this.rawpassword = rawpassword;
	}

	public User(Long id, @NotBlank @Size(max = 300) @Email String email, @NotBlank @Size(max = 250) String password,
			String rawpassword, Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.rawpassword = rawpassword;
		this.roles = roles;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
