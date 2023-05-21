package com.example.userservice.auth;

import com.example.userservice.Generators.TokenGenerator;
import com.example.userservice.Requests.ResetRequest;
import com.example.userservice.Requests.StudentRegisterRequest;
import com.example.userservice.Requests.TeacherRegisterRequest;
import com.example.userservice.Service.EmailService;
import com.example.userservice.user.User;
import com.example.userservice.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository userRepository;

  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  private final TokenGenerator tokenGenerator;

  @PostMapping("/register/student")
  public ResponseEntity<HashMap> registerStudent(
          @RequestBody StudentRegisterRequest request
  ) throws Exception {
    System.out.println(request.getEmail() +" " +request.getFirstname()+" " +request.getLastname() );
    String token = service.registerStudent(request);
    //make confirmation code with 5 digits
    emailService.sendEmail(request.getEmail(),"http://localhost:4200/emailvalidation?token="+token+"&email="+request.getEmail() ,token);
    HashMap<String , String> hashMap = new HashMap<>();
    hashMap.put("message" , "Email sent successfully");
    return ResponseEntity.ok(hashMap);
  }
  @PostMapping("/register/teacher")
  public ResponseEntity<HashMap> registerTeacher(
          @RequestBody TeacherRegisterRequest request
  ) throws Exception {
    System.out.println(request.getEmail() +" " +request.getFirstname()+" " +request.getLastname() );
    String token = service.registerTeacher(request);
    //make confirmation code with 5 digits
    emailService.sendEmail(request.getEmail(),"http://localhost:8080/api/v1/auth/emailValidation?token="+token+"&email="+request.getEmail() ,token);
    HashMap<String , String> hashMap = new HashMap<>();
    hashMap.put("message" , "Email sent successfully");
    return ResponseEntity.ok(hashMap);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    System.out.println("LLLLLLogin");
    return ResponseEntity.ok(service.authenticate(request));
  }

  @GetMapping("/emailValidation")
  public ResponseEntity<HashMap> confirmation(@RequestParam String token,@RequestParam String email) throws Exception {

    System.out.println(token);
    System.out.println(email);

    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()){
      HashMap<String,String> hashMap = new HashMap<>();
      hashMap.put("message" , "user with email not found");
      return ResponseEntity.status(400).body(hashMap);
    }
    User user =userOptional.get();
    System.out.println(user.getEmail());
    // Split the token into its random and email parts
    /*String email = tokenGenerator.decrypt(token);
    System.out.println(email);
    // Look up the user by email in your database
    Optional<User> optionalUser = userRepository.findByEmail(email);*/
    HashMap<String,String> hashMap = new HashMap<>();

    // Check that the user exists and that the token matches
    if (user.getConfirmationToken().equals(token)) {
      // Update the user's account status and clear the validation token
      user.setEnabled(true);
      userRepository.save(user);
      hashMap.put("message" ,"Email validated successfully!");
      return ResponseEntity.ok(hashMap);
    } else {
      hashMap.put("message" ,"Invalid token or user not found.");
      return ResponseEntity.status(400).body(hashMap);
    }

  }

  @PostMapping("/forgotPassword")
  public String forget(@RequestBody HashMap<String,String> hashMap){
    String email = hashMap.get("email");
    Optional<User> optional = userRepository.findByEmail(email);
    if (optional.isPresent()){
      emailService.forgetMail(email);
      return "Email Sent";
    }
    return "No User with such Email";
  }

  @PatchMapping("/resetPassword")
  public String reset(@RequestBody ResetRequest request) throws Exception {
    if (request.getPassword() == null){
      throw new Exception("Empty Password");
    }
    System.out.println(request.getPassword());

    String email = tokenGenerator.verifyToken(request.getToken());
    User user = userRepository.findByEmail(email).get();
    System.out.println("old : " + user.getPassword());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);
    return "Password Updated Correctly";

  }


}
