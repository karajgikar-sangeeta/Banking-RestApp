package com.banking.svkbanking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userRole")
public class UserRole {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleId")
	private Integer roleId;
	
	@Column(name = "roleName")
	private String roleName;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
//
//	@OneToMany(mappedBy = "userRole")
//	private List<User> users;
	
}
