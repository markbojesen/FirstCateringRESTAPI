package com.firstcateringltd.controller;

import com.firstcateringltd.crud.EmployeeCrud;
import com.firstcateringltd.model.Employee;
//import com.firstcateringltd.security.PasswordHasher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private EmployeeCrud employeeService;

    @Autowired
    private WebApplicationContext wac;

    private Employee employee;
//    private PasswordHasher hasher;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(wac).
                build();

//        employee = new Employee();
//        employee.setEmployeeId((long) 1);
//        employee.setFirstName("John");
//        employee.setLastName("Johnson");
//        employee.setCredit(100.0);
//        employee.setDataCard("001ABC");
//        employee.setMobile("+447403764288");
//        employee.setEmail("johnjohnson@gmail.com");
//        employee.setCreditCardNumber("8472539283740184");
//        employee.setCardRegistered(true);
//
//        employeeService.save(employee);
//        List<Employee> employees = Arrays.asList(employee);
//        given(employeeService.findAll()).willReturn(employees);
    }

    @Test
    public void getAllEmployeesAPI() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertEquals(employee.getFirstName(), "John");
    }
}

//    @Test
//    public void findAllEmployee() throws Exception {
//        hasher = new PasswordHasher();
//        employee = new Employee();
//
//        employee.setEmployeeId((long) 1);
//        employee.setFirstName("John");
//        employee.setLastName("Johnson");
//        employee.setCredit(100.0);
//        employee.setDataCard("001ABC");
//        employee.setMobile("+447403764288");
//        employee.setEmail("johnjohnson@gmail.com");
//        employee.setCreditCardNumber("8472539283740184");
//        employee.setCardRegistered(true);
//
//        List<Employee> employees = Arrays.asList(employee);
//
//        given(employeeService.findAll()).willReturn(employees);
//
//        this.mockMvc.perform(get("/api/employees"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[\n" +
//                        "    {\n" +
//                        "        \"firstName\": \"John\",\n" +
//                        "        \"lastName\": \"Johnson\",\n" +
//                        "        \"creditCardNumber\": \"8472539283740184\",\n" +
//                        "        \"employeeId\": 1,\n" +
//                        "        \"mobile\": \"+447403764288\",\n" +
//                        "        \"credit\": 100.0,\n" +
//                        "        \"email\": \"johnjohnson@gmail.com\",\n" +
//                        "        \"cardRegistered\": true,\n" +
//                        "        \"dataCard\": \"001ABC\",\n" +
//                        "    }]"));
//    }