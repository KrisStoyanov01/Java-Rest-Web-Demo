package org.sirma.rest_research.web.controllers;

import org.modelmapper.ModelMapper;
import org.sirma.rest_research.domain.entites.Student;
import org.sirma.rest_research.domain.models.service.StudentServiceModel;
import org.sirma.rest_research.domain.models.view.StudentListViewModel;
import org.sirma.rest_research.error.ResourceNotFoundException;
import org.sirma.rest_research.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StudentController{

    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentController(StudentService studentService, ModelMapper modelMapper) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/students")
    public List<StudentListViewModel> getAllStudents() {
        return this.studentService.findAllStudents()
                .stream()
                .map(s -> this.modelMapper.map(s, StudentListViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/students")
    public StudentServiceModel createStudent(@Valid @RequestBody Student student) {
        return this.studentService.addStudent(this.modelMapper.map(student, StudentServiceModel.class));
    }

    @GetMapping("/students/{id}")
    public StudentServiceModel getStudentById(@PathVariable(value = "id") String studentId) {
        return this.studentService.findStudentById(studentId);
    }

    @PutMapping("/students/{id}")
    public StudentServiceModel updateStudent(@PathVariable(value = "id") String studentId,
                           @Valid @RequestBody Student studentDetails) {

        return this.studentService.editStudent(studentId, this.modelMapper.map(studentDetails, StudentServiceModel.class));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") String studentId) {
        StudentServiceModel student = this.studentService.findStudentById(studentId);

        this.studentService.deleteStudent(student.getId());

        return ResponseEntity.ok().build();
    }
}
