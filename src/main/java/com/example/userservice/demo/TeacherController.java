package com.example.userservice.demo;

import com.example.userservice.Globals.ErrorResponse;
import com.example.userservice.Globals.GStudent;
import com.example.userservice.Globals.GTeacher;
import com.example.userservice.Repos.TeacherRepository;
import com.example.userservice.Repos.UserRepository;
import com.example.userservice.Requests.UpdateRequest;
import com.example.userservice.user.Student;
import com.example.userservice.user.Teacher;
import com.example.userservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {


  private final TeacherRepository teacherRepository;
  private final UserRepository userRepository;
  @GetMapping
  public ResponseEntity<?> sayHello() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Teacher teacher = teacherRepository.findByUserEmail(userDetails.getUsername());
    if (teacher == null){
      return ResponseEntity.status(402).body(new ErrorResponse("could not find teacher details"));
    }

    GTeacher gTeacher = serialize(teacher.getUser(),teacher);
    return ResponseEntity.ok(gTeacher);
  }

  @PutMapping("/update")
  public ResponseEntity<?> update(@RequestBody UpdateRequest request){

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Teacher teacher = teacherRepository.findByUserEmail(userDetails.getUsername());
    User user = userRepository.findByEmail(userDetails.getUsername()).get();

    if (user == null){
      return ResponseEntity.status(402).body(new ErrorResponse("could not find teacher"));
    }

    if (request.getNom() != null ) {
      if (!request.getNom().equals("")){
        user.setLastname(request.getNom());
      }
    }
    if (request.getPrenom() != null ) {
      if (!request.getPrenom().equals("")){
        user.setFirstname(request.getPrenom());
      }
    }
    if (request.getAdresse() != null ) {
      if (!request.getAdresse().equals("")){
        user.setAdresse(request.getAdresse());
      }
    }
    if (request.getVille() != null) {
      if (!request.getVille().equals("")){
        user.setVille(request.getVille());
      }
    }
    if (request.getDateNaissance() != null ) {
      if (!request.getDateNaissance().equals("")){
        user.setDateNaissance(request.getDateNaissance());
      }
    }
    if (request.getEtablissement() != null) {
      if (!request.getEtablissement().equals("")){
        user.setEtablissement(request.getEtablissement());
      }
    }
    if (request.getTelephone() != null) {
      if (!request.getTelephone().equals("")){
        user.setTelephone(request.getTelephone());
      }
    }
    if (request.getCodePostal() != null) {
      if (!request.getCodePostal().equals("")){
        user.setCodePostal(request.getCodePostal());
      }
    }
    if (request.getBio() != null ) {
      if (!request.getBio().equals("")){
        user.setBio(request.getBio());
      }
    }
    if (request.getNiveau() != null ) {
      if (!request.getNiveau().equals("")){
        teacher.setNiveau(request.getNiveau());
      }
    }

    userRepository.save(user);
    teacherRepository.save(teacher);
    GTeacher gTeacher = serialize(user,teacher);
    return ResponseEntity.ok(gTeacher);
  }

  public GTeacher serialize(User user, Teacher teacher){
    return GTeacher.builder()
            .id(user.getId())
            .nom(user.getLastname())
            .prenom(user.getFirstname())
            .adresse(user.getAdresse())
            .date_naissance(user.getDateNaissance())
            .ville(user.getVille())
            .email(user.getEmail())
            .etablissement(user.getEtablissement())
            .imageUrl(user.getImageUrl())
            .telephone(user.getTelephone())
            .niveau(teacher.getNiveau())
            .module(teacher.getModule())
            .codePostal(user.getCodePostal())
            .build();
  }

}
