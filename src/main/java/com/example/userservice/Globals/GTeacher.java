package com.example.userservice.Globals;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GTeacher {

    private Integer id;
    private String email;
    private String adresse;
    private String prenom;
    private String etablissement;
    private String telephone;
    private String nom;
    private Date date_naissance;
    private String ville;
    private String imageUrl;
    private String niveau;
    private String module;
    private String codePostal;
    private String bio;
}
