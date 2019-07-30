package com.firstcateringltd.repository;

import com.firstcateringltd.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Employee findByDataCard(String dataCard);
}
