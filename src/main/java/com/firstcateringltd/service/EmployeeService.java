package com.firstcateringltd.service;

import com.firstcateringltd.model.Employee;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface EmployeeService {

    Employee save(Employee employee);

    void deleteById(Long id);

    Iterable<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee findByDataCard(String dataCard);
}
