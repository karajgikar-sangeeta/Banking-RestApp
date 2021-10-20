package com.banking.svkbanking.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.svkbanking.BankingApplication;
import com.banking.svkbanking.entity.Account;
import com.banking.svkbanking.entity.AccountTypes;
import com.banking.svkbanking.entity.Transaction;
import com.banking.svkbanking.entity.TransactionType;
import com.banking.svkbanking.repository.AccountRepository;
import com.banking.svkbanking.repository.AccountTypeRepository;
import com.banking.svkbanking.repository.TransactionRepository;
import com.banking.svkbanking.repository.TransactionTypeRepository;
import com.banking.svkbanking.repository.UserRepository;



@CrossOrigin(origins = "http://localhost:4200, http://localhost:8080")
@RestController
@RequestMapping("/api")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired private AccountRepository accountRepo;
	
	@Autowired private AccountTypeRepository accountTypeRepo;
	
	@Autowired private UserRepository userRepo;
	
	@Autowired private TransactionRepository transactionRepo;
	
	@Autowired private TransactionTypeRepository transactionTypeRepo;
	
	@GetMapping("/accounts/accountTypes")
	public ResponseEntity<List<AccountTypes>> getAllAccountTypes() {
		List<AccountTypes> accountTypesList = accountTypeRepo.findAll();
		return ResponseEntity.ok(accountTypesList); 
		
	}
	
	@GetMapping("/accounts/transactionTypes")
	public ResponseEntity<List<TransactionType>> getAllTransactionTypes() {
		List<TransactionType> transactionTypesList = transactionTypeRepo.findAll();
		return ResponseEntity.ok(transactionTypesList); 
	}
	
	@GetMapping("/accounts/unapproved")
	public ResponseEntity<List<Account>> getAllUnapprovedAccounts() throws ResourceNotFoundException {
		
		logger.info("Getting the list of unapproved accounts");
		
		List<Account> allAccounts = accountRepo.findAll();
		allAccounts.removeIf(x -> x.isActive() == true);
		return ResponseEntity.ok(allAccounts);
	}
	
	@PostMapping("/accounts/approve")
	public ResponseEntity<Boolean> approveAccounts(@RequestBody ArrayList<Long> accountIds) throws ResourceNotFoundException {
		
		List<Account> accountsToActivate = new ArrayList<Account>();
		
		List<Account> accounts = accountRepo.findAllById(accountIds);
		for (Account account : accounts) {
			if (!account.isActive()) {
				accountsToActivate.add(account);
			}
		}
		
		if (accountsToActivate.size() > 0) {
			for (Account account: accountsToActivate) {
				if (!account.isActive()) {
					account.setActive(true);
				}
			}
			accountRepo.saveAllAndFlush(accountsToActivate);
		}
		
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("/accounts/{id}")
	public ResponseEntity<List<Account>> getAllAccountsByUserId(
			@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		
		List<Account> accounts = new ArrayList<Account>();
		
		List<Account> allAccounts = accountRepo.findAll();
		if (allAccounts != null && allAccounts.size() > 0) {
			allAccounts.forEach(x -> {
				if(x.getUser().getUserId() == userId) {
					accounts.add(x);
				}
			});			
		}
		return ResponseEntity.ok(accounts);
	}
	
	@PostMapping("/accounts")
	public ResponseEntity<?> createAccount(@RequestBody Account account) throws ResourceNotFoundException {
		Account accountToSave = new Account();
		accountToSave.setAccountType(account.getAccountType());
		accountToSave.setAccountBalance(account.getAccountBalance());
		accountToSave.setUser(account.getUser());
		
		var savedAccount = accountRepo.save(accountToSave);
		return ResponseEntity.ok(savedAccount);
	}
	
	@PostMapping("/accounts/deposit")
	public ResponseEntity<?> depositAmount(@RequestBody Transaction transaction) throws ResourceNotFoundException {
		
		var userExists = userRepo.findById(transaction.getUserId());
		if (!userExists.isPresent()) {
			System.out.println("Users does not exists");
			return ResponseEntity.badRequest().body("User does not exist " + transaction.getUserId());
		}
		System.out.println("Users exists");
		var accountExists = accountRepo.findById(transaction.getAccountId());
		if (!accountExists.isPresent()) {
			System.out.println("Account does not exists");
			return ResponseEntity.badRequest().body("Invalid account " + transaction.getAccountId());	
		}
		
		Account account = accountExists.get();
		if (!account.isActive()) {
			System.out.println("Account id " + account.getAccountId() + " is not active");
			return ResponseEntity.badRequest().body("AccountId: " + transaction.getAccountId() + " is inactive");
		}
		
		var transactionTypeExists = transactionTypeRepo.findById(transaction.getTransactionType().getTransactionTypeId());
		if (!transactionTypeExists.isPresent()) {
			System.out.println("Transaction type does exists");
			return ResponseEntity.badRequest().body("Invalid transaction type " + transaction.getTransactionType().getTransactionTypeId());
		}
		
		var transactionType = transactionTypeExists.get();
		if (!transactionType.getTransactionTypeId().equals(2)) {
			System.out.println("Invalid transaction type " + transactionType);
			return ResponseEntity.badRequest().body("Invalid transaction type - should be 'credit' type");
		}
		
		
		Float accountBalance = account.getAccountBalance(); // + transaction.getAmount();
		
		if (account.getAccountType().getTypeId().equals(2)) {
			// mortgage account
			accountBalance -= transaction.getAmount();
		} else {
			accountBalance += transaction.getAmount();
		}
		
		account.setAccountBalance(accountBalance);
		System.out.println("Account Balance : " + accountBalance);
		
		accountRepo.save(account);
		transactionRepo.save(transaction);
		
		return ResponseEntity.ok(null);
		
	}
	
	@PostMapping(path = "/accounts/withdraw", produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> withdrawAmount(@RequestBody Transaction transaction) throws ResourceNotFoundException {
		
		var userExists = userRepo.findById(transaction.getUserId());
		if (!userExists.isPresent()) {
			return ResponseEntity.badRequest().body("User does not exist " + transaction.getUserId());
		}
		
		var accountExists = accountRepo.findById(transaction.getAccountId());
		if (!accountExists.isPresent()) {
			return ResponseEntity.badRequest().body("Invalid account " + transaction.getAccountId());	
		}
		
		var transactionTypeExists = transactionTypeRepo.findById(transaction.getTransactionType().getTransactionTypeId());
		if (!transactionTypeExists.isPresent()) {
			return ResponseEntity.badRequest().body("Invalid transaction type " + transaction.getTransactionType().getTransactionTypeId());
		}
		
		var transactionType = transactionTypeExists.get();
		if (!transactionType.getTransactionTypeId().equals(1)) {
			return ResponseEntity.badRequest().body("Invalid transaction type - should be 'dedit' type");
		}
		
		Account account = accountExists.get();
		if (account.getAccountType().getTypeId().equals(2)) {
			return ResponseEntity.badRequest().body("Invalid account type 'mortgage' for withdrawal");
		}		
		
		Float accountBalance = account.getAccountBalance() - transaction.getAmount();
		if (accountBalance < 0) {
			 return ResponseEntity.badRequest().body("Cannot withdraw amount of " + transaction.getAmount() + ": Insufficient funds");
		}
		
		account.setAccountBalance(accountBalance);
		accountRepo.save(account);
		transactionRepo.save(transaction);
				
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/accounts/transactions/{id}")
	public ResponseEntity<?> getUsersTransactions(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		
		List<Transaction> transactions = new ArrayList<Transaction>(); 
		
		List<Transaction> allTransactions = transactionRepo.findAll();
		if (allTransactions != null && allTransactions.size() > 0) {
			allTransactions.forEach((x) -> {
				if (x.getUserId() == userId) {
					transactions.add(x);
				}
			});
		}
		return ResponseEntity.ok(transactions);		
	}		
}





