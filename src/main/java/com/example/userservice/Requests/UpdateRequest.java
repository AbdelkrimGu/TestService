package com.example.userservice.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private String nom;
    private String prenom;
    private String adresse;
    private String ville;
    private Date dateNaissance;
    private String etablissement;
    private String telephone;

    private String codePostal;
    private String bio;
    private String niveau;

}
