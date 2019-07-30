package com.firstcateringltd.controller;

import com.firstcateringltd.crud.EmployeeCrud;
import com.firstcateringltd.exception.CardNotRegisteredException;
import com.firstcateringltd.exception.NotSufficientFundsException;
import com.firstcateringltd.exception.ResourceNotFoundException;
import com.firstcateringltd.model.Employee;
//import com.firstcateringltd.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeCrud employeeService;

    private final String WELCOME_MESSAGE = "Welcome ";
    private final String DOUBLE_TAP_MESSAGE = "Goodbye";

    /**
     * @return List of all employees
     */
    @GetMapping(value = "/employees", produces = "application/json")
    public List<Employee> findAllEmployee() {
        return (List<Employee>) employeeService.findAll();
    }

    /**
     * @return Specific employee based on ID
     * @param id
     * @exception ResourceNotFoundException
     */
    @GetMapping(value = "/employees/{id}", produces = "application/json")
    public Employee findById(@PathVariable("id") Long id) {
        return employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));
    }

    /**
     * @return Updated employee value
     * @param id
     * @param employee
     * @exception ResourceNotFoundException
     */
    @PutMapping(value = "/employees/{id}", produces = "application/json", consumes = "application/json")
    public Employee updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));
        return employeeService.save(employee);
    }

    /**
     * @return New Employee
     * @param employee
     */
    @PostMapping(value = "/employees/register", produces = "application/json", consumes = "application/json")
    public Employee addNewEmployee(@RequestBody Employee employee, String ccNumber) {
//        PasswordHasher pwHasher = new PasswordHasher();
//
//        employee.setCreditCardNumber(pwHasher.hassPassword(ccNumber));
        return employeeService.save(employee);
    }

    /**
     * @return Response 200 for success
     * @param id
     * @exception ResourceNotFoundException
     */
    @DeleteMapping(value = "/employees/{id}", consumes = "application/json")
    public void deleteEmployee(@PathVariable("id") Long id) {
        if (employeeService.findById(id).isPresent()) {
            employeeService.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No employee found with id: " + id);
        }
    }

    // --------------- CREDIT ---------------
    // --------------------------------------

    /**
     * @return Current amount of credit for employee.
     * @param id
     * @exception ResourceNotFoundException
     * @exception CardNotRegisteredException
     */
    @GetMapping(value = "/employees/credit/check/{id}", produces = "application/json")
    public Double checkCredit(@PathVariable("id") Long id) {
        Employee employeeCreditCheck = employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));

        if (!employeeCreditCheck.isCardRegistered()) {
            throw new CardNotRegisteredException();
        }

        return employeeCreditCheck.getCredit();
    }

    /**
     * @return Credit value after being topped up.
     * @param id
     * @param credit
     * @exception ResourceNotFoundException
     * @exception CardNotRegisteredException
     */
    @Modifying(clearAutomatically = true)
    @PutMapping(value = "/employees/credit/topup/{id}", produces = "application/json", consumes = "application/json")
    public Double topUpCredit(@PathVariable("id") Long id,
                                @RequestParam(required = true, name = "credit") Double credit) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));

        if (!employee.isCardRegistered()) {
            throw new CardNotRegisteredException();
        }

        Double newCredit;
        newCredit = employee.getCredit() + credit;
        employee.setCredit(newCredit);
        employeeService.save(employee);

        return employee.getCredit();
    }

    /**
     * @return Credit value af a withdraw.
     * @param id
     * @param credit
     * @exception ResourceNotFoundException
     * @exception CardNotRegisteredException
     * @exception NotSufficientFundsException
     */
    @PutMapping(value = "/employees/credit/withdraw/{id}", produces = "application/json", consumes = "application/json")
    public Double withdrawCredit(@PathVariable("id") Long id, @RequestParam(required = true, name = "credit") Double credit) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));

        if (!employee.isCardRegistered()) {
            throw new CardNotRegisteredException();
        }

        if (employee.getCredit() == 0) {
            throw new NotSufficientFundsException("There are not sufficient funds to make a withdrawal of that size: " + credit);
        }

        Double newCredit;
        newCredit = employee.getCredit() - credit;
        employee.setCredit(newCredit);
        employeeService.save(employee);

        return  employee.getCredit();
    }

    // ------------- MESSAGES --------------
    // -------------------------------------

    /**
     * When a card is presented and already has a registered data card, display below message
     * @return Welcome message based on registered data card
     * @param dataCard
     * @exception CardNotRegisteredException
     */
    @GetMapping(value = "/employees/scancard/{dataCard}", produces = "application/json")
    public String scanAndCheckDataCard(@PathVariable("dataCard") String dataCard) {
        Employee employee = employeeService.findByDataCard(dataCard);

        if (employee.getDataCard() == null) {
            throw new CardNotRegisteredException();
        }

        return WELCOME_MESSAGE + employee.firstName + " " + employee.lastName;
    }

    /**
     * Part imaginary method. When data card is double tapped/tapped a second time, display below message
     * @return Welcome message based on registered data card
     * @param dataCard
     * @exception CardNotRegisteredException
     */
    @GetMapping(value = "/employees/scancard/doubletap/{dataCard}")
    public String doubleTapDataCard(@PathVariable("dataCard") String dataCard) {
        Employee employee = employeeService.findByDataCard(dataCard);

        if (employee.getDataCard() == null) {
            throw new CardNotRegisteredException();
        }

        return DOUBLE_TAP_MESSAGE;
    }

}
