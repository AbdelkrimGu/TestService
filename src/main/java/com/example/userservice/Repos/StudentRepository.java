package com.example.userservice.Repos;

import com.example.userservice.user.Student;
import com.example.userservice.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Student findByUserEmail(@Param("email") String email);

    List<Student> findAllByUser_IdIn(List<Integer> userIds);
}