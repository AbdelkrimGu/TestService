package com.example.userservice.auth;

import com.example.userservice.Generators.TokenGenerator;
import com.example.userservice.Repos.StudentRepository;
import com.example.userservice.Requests.StudentRegisterRequest;
import com.example.userservice.Requests.TeacherRegisterRequest;
import com.example.userservice.config.JwtService;
import com.example.userservice.user.*;
import com.example.userservice.Repos.TeacherRepository;
import com.example.userservice.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private final TokenGenerator tokenGenerator;
  private final TeacherRepository teacherRepository;
  private final StudentRepository studentRepository;

  /*public String register(RegisterRequest request) throws Exception {
    if (request.getRole().toLowerCase().equals("admin")){
      throw new Exception("You can't add an admin");
    }
    String token = tokenGenerator.encrypt(request.getEmail());
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.valueOf(request.getRole()))
        .confirmationToken(token)
        .module(request.getModule())
        .build();
    repository.save(user);
    return token;
  }*/

  public String registerStudent(StudentRegisterRequest request) throws Exception {

    String token = tokenGenerator.encrypt(request.getEmail());
    Student student ;
    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.STUDENT)
            .confirmationToken(token)
            .telephone(request.getTelephone())
            .dateNaissance(request.getDateNaissance())
            .imageUrl("https://ui-avatars.com/api/?name="+request.getLastname()+"+"+request.getFirstname()+"&background=4035B8&color=ffffff")
            .build();
    repository.save(user);
    student = Student.builder()
            .id(null)
            .user(user)
            .niveauScolaire(request.getNiveau())
            .build();
    studentRepository.save(student);
    return token;
  }

  public String registerTeacher(TeacherRegisterRequest request) throws Exception {

    String token = tokenGenerator.encrypt(request.getEmail());
    Teacher teacher;
    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.TEACHER)
            .confirmationToken(token)
            .telephone(request.getTelephone())
            .dateNaissance(request.getDateNaissance())
            .build();
    repository.save(user);
    teacher = Teacher.builder()
            .id(null)
            .module(request.getModule())
            .niveau(request.getNiveau())
            .user(user)
            .build();
    teacherRepository.save(teacher);
    return token;
  }



  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    System.out.println(request.getEmail());
    System.out.println(request.getPassword());

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    System.out.println(user.getEmail());
    user.setConnexion(LocalDateTime.now());
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .role(user.getRole())
            .build();
  }

}
