package com.example.piggybank.metier;

import org.springframework.data.domain.Page;

import com.example.piggybank.entities.Compte;
import com.example.piggybank.entities.Operation;

import java.util.Optional;

public interface IBankMetier {

    public Optional<Compte> consulterCompter(String codeCpte);
    public void verser(String codeCpte , double montant);
    public void reterait(String codeCpte , double montant);
    public void verement(String codeCpte1 , String codeCpte2 , double montant);
    public Page<Operation> listOperation(String codeCpte , int page , int size);
    Optional<Compte> findById(String id);

}
