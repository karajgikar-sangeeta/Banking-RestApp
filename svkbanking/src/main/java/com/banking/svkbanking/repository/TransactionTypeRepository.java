package com.banking.svkbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.svkbanking.entity.TransactionType;



@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {

}
