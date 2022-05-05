package com.brody.ebankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brody.ebankingbackend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
