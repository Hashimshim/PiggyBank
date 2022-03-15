package com.example.piggybank.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.piggybank.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
