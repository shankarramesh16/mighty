package com.team.mighty.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the roles database table.
 * 
 */
@Entity
@Table(name="roles")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String description;

	private String name;

	private String type;

	//bi-directional many-to-one association to AdminUser
	@OneToMany(mappedBy="role")
	private List<AdminUser> adminUsers;

	public Role() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AdminUser> getAdminUsers() {
		return this.adminUsers;
	}

	public void setAdminUsers(List<AdminUser> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public AdminUser addAdminUser(AdminUser adminUser) {
		getAdminUsers().add(adminUser);
		adminUser.setRole(this);

		return adminUser;
	}

	public AdminUser removeAdminUser(AdminUser adminUser) {
		getAdminUsers().remove(adminUser);
		adminUser.setRole(null);

		return adminUser;
	}

}