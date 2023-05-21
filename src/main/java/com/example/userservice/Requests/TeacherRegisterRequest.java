package com.example.userservice.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private String module;
  private String niveau;
  private String telephone;
  private Date dateNaissance;
}
