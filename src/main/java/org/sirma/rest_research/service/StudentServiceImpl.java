package org.sirma.rest_research.service;

import org.modelmapper.ModelMapper;
import org.sirma.rest_research.domain.entites.Student;
import org.sirma.rest_research.domain.models.service.StudentServiceModel;
import org.sirma.rest_research.error.ResourceNotFoundException;
import org.sirma.rest_research.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentServiceModel addStudent(StudentServiceModel studentServiceModel) {
        Student student = this.modelMapper.map(studentServiceModel, Student.class);
        try {
            this.studentRepository.saveAndFlush(student);
            return studentServiceModel;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentServiceModel> findAllStudents() {
        return this.studentRepository.findAll()
                .stream()
                .map(s -> this.modelMapper.map(s, StudentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudentServiceModel findStudentById(String id) {
        return this.studentRepository.findById(id)
                .map(s -> {
                    StudentServiceModel studentServiceModel = this.modelMapper.map(s, StudentServiceModel.class);
                    this.studentRepository.findById(studentServiceModel.getId());

                    return studentServiceModel;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    @Override
    public boolean deleteStudent(String id) {
        try {
            this.studentRepository.deleteById(id);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public StudentServiceModel editStudent(String id, StudentServiceModel studentServiceModel) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        student.setFirstName(studentServiceModel.getFirstName());
        student.setLastName(studentServiceModel.getLastName());
        student.setAge(studentServiceModel.getAge());

        return this.modelMapper.map(this.studentRepository.saveAndFlush(student), StudentServiceModel.class);
    }
}
