package com.example.userservice.Repos;

import com.example.userservice.user.Student;
import com.example.userservice.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t WHERE t.user.email = :email")
    Teacher findByUserEmail(@Param("email") String email);

    List<Teacher> findAllByUser_IdIn(List<Integer> userIds);

    Teacher findByUserId(Integer id);


}