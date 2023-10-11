package com.example.demo.Repository;

import com.example.demo.Model.Student;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class StudentRepository implements StudentRepositoryInterface {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void saveStudent(Student student) {
    }

    @Override
    public Student findStudentByName(String name) {
        return entityManager.find(Student.class, "andre");
    }
}
