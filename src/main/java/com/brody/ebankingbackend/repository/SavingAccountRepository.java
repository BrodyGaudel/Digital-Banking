package com.brody.ebankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brody.ebankingbackend.entities.SavingAccount;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, String> {

}
