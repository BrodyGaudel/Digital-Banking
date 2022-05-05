package com.brody.ebankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brody.ebankingbackend.entities.CurrentAccount;

public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, String> {

}
