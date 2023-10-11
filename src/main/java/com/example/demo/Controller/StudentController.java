package com.example.demo.Controller;

import com.example.demo.Service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/students")
    public void startSomething() {
        studentService.getOne();
        System.out.println("teste");
    }
}
