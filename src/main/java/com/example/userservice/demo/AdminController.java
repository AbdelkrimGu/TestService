package com.example.userservice.demo;

import com.example.userservice.Repos.UserRepository;
import com.example.userservice.Requests.TeacherRegisterRequest;
import com.example.userservice.Service.EmailService;
import com.example.userservice.auth.AuthenticationService;
import com.example.userservice.user.Role;
import com.example.userservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {


    private final UserRepository userRepository;
    private final AuthenticationService service;
    private final EmailService emailService;
    @GetMapping
    public ResponseEntity<User> sayHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/teacher/create")
    public ResponseEntity<String> teacher(@RequestBody TeacherRegisterRequest request) throws Exception{

        String token = service.registerTeacher(request);
        emailService.sendEmail(request.getEmail(),"http://localhost:8080/api/v1/auth/emailValidation?token="+token , token);
        return ResponseEntity.ok("Email sent successfully");
    }

    @GetMapping("/teacher/count")
    public ResponseEntity<Long> teachersCount(){
        return ResponseEntity.ok(userRepository.countByRole(Role.TEACHER));
    }
    @GetMapping("/student/count")
    public ResponseEntity<Long> studentsCount(){
        return ResponseEntity.ok(userRepository.countByRole(Role.STUDENT));
    }
    @GetMapping("/admin/count")
    public ResponseEntity<Long> adminsCount(){
        return ResponseEntity.ok(userRepository.countByRole(Role.ADMIN));
    }

}
