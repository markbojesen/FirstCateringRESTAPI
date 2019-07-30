package com.firstcateringltd.controller;

import com.firstcateringltd.crud.EmployeeCrud;
import com.firstcateringltd.model.Employee;
import com.firstcateringltd.security.BasicAuthenticationPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.firstcateringltd.security.CreditCardHasher;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private EmployeeCrud employeeService;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private BasicAuthenticationPoint bsp;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private BCrypt bCrypt;

    private Employee employee;
    private CreditCardHasher creditCardHasher;
    private String jsonResponseBody = "[{\"firstName\":\"John\",\"lastName\":\"Johnson\",\"creditCardNumber\":\"8472539283740184\",\"employeeId\":1,\"mobile\":\"+447403764288\",\"credit\":100.0,\"email\":\"johnjohnson@gmail.com\",\"cardRegistered\":true,\"dataCard\":\"001ABC\",\"welcomeMessage\":null}]";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(wac).
                build();

        employee = new Employee();
        employee.setEmployeeId(new Long(1));
        employee.setFirstName("John");
        employee.setLastName("Johnson");
        employee.setCredit(100.0);
        employee.setDataCard("001ABC");
        employee.setMobile("+447403764288");
        employee.setEmail("johnjohnson@gmail.com");
        employee.setCreditCardNumber("8472539283740184");
        employee.setCardRegistered(true);

        employeeService.save(employee);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneEmployee() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertEquals(jsonResponseBody, "[{\"firstName\":\"John\",\"lastName\":\"Johnson\",\"creditCardNumber\":\"8472539283740184\",\"employeeId\":1,\"mobile\":\"+447403764288\",\"credit\":100.0,\"email\":\"johnjohnson@gmail.com\",\"cardRegistered\":true,\"dataCard\":\"001ABC\",\"welcomeMessage\":null}]");
        Assert.assertEquals(new Long(1), employee.getEmployeeId());
    }

    @Test
    public void testEmployeeService_findAll_ShouldReturnAListOfEmployees() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        List<Employee> employees = Arrays.asList(employee);
        given(employeeService.findAll()).willReturn(employees);
        Assert.assertTrue(employees.size() > 0);
    }

    @Test
    public void testPasswordHashing() {
        creditCardHasher = new CreditCardHasher();
        String plainPassword  = "MySecretPassword";
        String hashedPassword = creditCardHasher.hashCreditCard(plainPassword);
        Assert.assertTrue(hashedPassword.length() == 60);
        Assert.assertTrue(bCrypt.checkpw(plainPassword, hashedPassword));
    }
}
