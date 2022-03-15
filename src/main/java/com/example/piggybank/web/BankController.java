package com.example.piggybank.web;

import com.example.piggybank.entities.Compte;
import com.example.piggybank.metier.IBankMetier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class BankController {

    @Autowired
    public IBankMetier iBankMetier;

    @RequestMapping(value = "/operations")
    public String index(){
        return "comptes";
    }


    @RequestMapping(value = "/consulterCompte" , method = RequestMethod.GET)
    public String consulter(Model model , String codeCompte ,
                            @RequestParam(name = "page",defaultValue = "0") int page ,
                            @RequestParam(name = "size",defaultValue = "4") int size){
        try{
            Compte cp = iBankMetier.consulterCompter(codeCompte).get();
           
            Page listOperations = iBankMetier.listOperation(codeCompte,page,size);
            model.addAttribute("compte",cp);
            model.addAttribute("listOperations",listOperations);
            int[] pages = new int[listOperations.getTotalPages()];
            model.addAttribute("pages",pages);
            model.addAttribute("codeCompte",codeCompte);
        }catch (Exception e){
            model.addAttribute("exception","Compte introuvable");
        }

        return "comptes";
    }
    @RequestMapping(value="/save" ,method = RequestMethod.POST )
    public String saveOperation(Model model ,  String typeOperation , String codeCompte , double montant , String codeCompte2){
      try{
          if(typeOperation.equals("VERS")){
              iBankMetier.verser(codeCompte,montant);
          }        if(typeOperation.equals("RETR")){
              iBankMetier.reterait(codeCompte,montant);
          }  if(typeOperation.equals("VIR")){
              iBankMetier.verement(codeCompte,codeCompte2,montant);
          }
      }catch (Exception e){
          model.addAttribute("exception",e);
          return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();
      }

        return "redirect:/consulterCompte?codeCompte="+codeCompte;
    }
    @RequestMapping(value = "/")
    public String home(){
        return "redirect:operations";
    }

}
