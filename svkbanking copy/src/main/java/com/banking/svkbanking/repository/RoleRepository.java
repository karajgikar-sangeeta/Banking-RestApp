package com.banking.svkbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.svkbanking.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);
}
