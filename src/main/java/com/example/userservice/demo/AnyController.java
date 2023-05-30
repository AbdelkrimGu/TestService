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

    @GetMapping("tutorial/disable")
    public ResponseEntity<Boolean> deactivateTutorial(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        User user = optionalUser.get();
        user.setTutorial(false);

        userRepository.save(user);

        return ResponseEntity.ok(false);
    }

}
