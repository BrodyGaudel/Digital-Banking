package com.brody.ebankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brody.ebankingbackend.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}
