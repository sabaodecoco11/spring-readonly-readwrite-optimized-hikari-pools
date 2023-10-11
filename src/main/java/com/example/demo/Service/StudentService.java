package com.example.demo.Service;

import com.example.demo.Repository.StudentRepositoryInterface;
import lombok.AllArgsConstructor;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepositoryInterface studentRepository;

    public void getOne() {
        var student = studentRepository.findStudentByName("andre");

        System.out.println(student.getName());
    }
}
