package com.example.piggybank.metier;

import com.example.piggybank.dao.ClientRepository;
import com.example.piggybank.dao.CompteRepository;
import com.example.piggybank.dao.OperationRepository;
import com.example.piggybank.entities.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class IBankMetierImp implements IBankMetier {
    @Autowired
    public CompteRepository compteRepository;

    @Autowired
    public OperationRepository operationRepository;
    @Autowired
    public ClientRepository clientRepository;


    @Override
    public Optional<Compte> consulterCompter(String codeCpte) {
        Optional<Compte> cp = compteRepository.findById(codeCpte);       
        if(cp==null) throw new RuntimeException("Compte Introuvable!");
        return cp;
    }

    @Override
    public Optional<Compte> findById(String codeCpte) {

        return compteRepository.findById(codeCpte);
    }
    @Override
    public void verser(String codeCpte, double montant) {
        if(findById(codeCpte).isPresent()){
        Optional<Compte> cp = findById(codeCpte);
        Versement v = new Versement(new Date() , montant, cp.get());
        operationRepository.save(v);
        cp.get().setSolde(cp.get().getSolde()+montant);
        compteRepository.save(cp.get());}

    }

    @Override
    public void reterait(String codeCpte, double montant) {
        Optional<Compte> cp = findById(codeCpte);

        if (findById(codeCpte).isPresent()) {
            if (cp.get() instanceof CompteCourant) {
                if (cp.get().getSolde() < montant) {
                    throw new RuntimeException("Solde insuffisant");

                } else {
                    Retrait r = new Retrait(new Date(), montant, cp.get());
                    operationRepository.save(r);
                    cp.get().setSolde(cp.get().getSolde() - montant);
                    compteRepository.save(cp.get());
                }
            }

        }
    }

    @Override
    public void verement(String codeCpte1, String codeCpte2, double montant) {
        if (!codeCpte1.equals(codeCpte2)) {
            reterait(codeCpte1,montant);
            verser(codeCpte2,montant);
        }
        else{
            throw new RuntimeException("You can't send money to yourself");
        }

    }

    @Override
    public Page<Operation> listOperation(String codeCpte, int page, int size) {
        return operationRepository.listOperatiion(codeCpte,PageRequest.of(page,size));
    }
}
