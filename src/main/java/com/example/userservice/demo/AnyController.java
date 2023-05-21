package com.example.userservice.demo;

import com.example.userservice.Globals.GTeacher;
import com.example.userservice.Globals.Id;
import com.example.userservice.Repos.TeacherRepository;
import com.example.userservice.Repos.UserRepository;
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
@RequestMapping("/api/v1/any")
@RequiredArgsConstructor
public class AnyController {


    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping
    public ResponseEntity<User> sayHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user.get());

    }



    public GTeacher serialize(User user, Teacher teacher){

        System.out.println(user.getBio());
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
                .codePostal(user.getCodePostal())
                .bio(user.getBio())
                .build();
    }

}
