package com.brody.ebankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brody.ebankingbackend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}
