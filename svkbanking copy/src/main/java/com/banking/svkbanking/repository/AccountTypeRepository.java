package com.banking.svkbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.svkbanking.entity.AccountTypes;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypes, Integer> {
	
}
