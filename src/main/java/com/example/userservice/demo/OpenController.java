package com.example.userservice.demo;

import com.example.userservice.Globals.GStudent;
import com.example.userservice.Globals.GTeacher;
import com.example.userservice.Globals.StudentId;
import com.example.userservice.Globals.TeacherId;
import com.example.userservice.Repos.StudentRepository;
import com.example.userservice.Repos.TeacherRepository;
import com.example.userservice.Repos.UserRepository;
import com.example.userservice.user.Student;
import com.example.userservice.user.Teacher;
import com.example.userservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/open")
@RequiredArgsConstructor
public class OpenController {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/")
    public ResponseEntity<?> teachers(@RequestBody TeacherId teacherIds){

        List<Integer> list = teacherIds.getTeacherIds();
        List<Teacher> teachers = teacherRepository.findAllByUser_IdIn(list);
        List<GTeacher> gTeachers = teachers.stream()
                .map(this::serializet)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gTeachers);
    }

    @PostMapping("/students")
    public ResponseEntity<?> students(@RequestBody StudentId studentIds){

        List<Integer> list = studentIds.getStudents();
        System.out.println(studentIds);
        List<Student> students = studentRepository.findAllByUser_IdIn(list);
        List<GStudent> gStudents = students.stream()
                .map(this::serializes)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gStudents);
    }

    @GetMapping("/teacher")
    public ResponseEntity<GTeacher> teacher(@RequestParam("id") Integer id){
        System.out.println(id);
        Teacher teacher = teacherRepository.findByUserId(id);
        return ResponseEntity.ok(serializet(teacher));
    }


    public GTeacher serializet(Teacher teacher){
        User user = teacher.getUser();

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
                .bio(user.getBio())
                .build();
    }

    public GStudent serializes(Student student){
        User user = student.getUser();

        return GStudent.builder()
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
                .niveau_scolaire(student.getNiveauScolaire())
                .codePostal(user.getCodePostal())
                .bio(user.getBio())
                .build();
    }

}
