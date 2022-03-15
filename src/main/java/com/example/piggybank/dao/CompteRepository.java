package com.example.piggybank.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.piggybank.entities.Compte;

public interface CompteRepository extends JpaRepository<Compte , String> {
}
