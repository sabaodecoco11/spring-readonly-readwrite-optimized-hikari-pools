package com.example.demo.Repository;

import com.example.demo.Model.Student;

public interface StudentRepositoryInterface {
    void saveStudent(Student student);
    Student findStudentByName(String name);
}
