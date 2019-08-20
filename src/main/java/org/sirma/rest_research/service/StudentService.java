package org.sirma.rest_research.service;

import org.sirma.rest_research.domain.models.service.StudentServiceModel;

import java.util.List;

public interface StudentService {
    StudentServiceModel addStudent(StudentServiceModel studentServiceModel);

    List<StudentServiceModel> findAllStudents();

    StudentServiceModel findStudentById(String id);

    boolean deleteStudent(String id);

    StudentServiceModel editStudent(String id, StudentServiceModel studentServiceModel);
}
