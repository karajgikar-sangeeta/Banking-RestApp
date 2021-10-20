package com.banking.svkbanking.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "userId") private Long userId;	
	@Column(name = "password") @NotEmpty private String password;	
	@Column(name = "fullName") @NotEmpty private String fullName;	
	@Column(name = "email") 
	@NotEmpty
	@Email
	private String email;
	
	@Column(name = "phoneNumber") private String phoneNumber;	
	@Column(name = "dateCreated") private Date dateCreated;	
	@ManyToMany(fetch=FetchType.EAGER) @JoinTable(name="user_role", joinColumns= @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name = "role_id")) private List<Role> roles;
	@OneToOne(fetch=FetchType.EAGER) @JoinColumn(name = "addressId") 
	private UserAddress userAddress;
	
	
	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	public Long getUserId() {
		return userId;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
		
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }
	
}
