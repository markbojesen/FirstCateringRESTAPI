package com.firstcateringltd.controller;

import com.firstcateringltd.crud.EmployeeCrud;
import com.firstcateringltd.model.Employee;
import com.firstcateringltd.security.BasicAuthenticationPoint;
import com.firstcateringltd.security.CreditCardHasher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        creditCardHasher = new CreditCardHasher();

        employee.setEmployeeId(new Long(1));
        employee.setFirstName("John");
        employee.setLastName("Johnson");
        employee.setCredit(100.0);
        employee.setDataCard("001ABC");
        employee.setMobile("+447403764288");
        employee.setEmail("johnjohnson@gmail.com");
        employee.setCreditCardNumber(creditCardHasher.hashCreditCard("8472539283740184"));
        employee.setCardRegistered(true);

        employeeService.save(employee);
    }

    @Test
    public void testGetAllEmployeesEndpoint() throws Exception {
        this.mockMvc.perform(get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAssertJsonOfOneEmployee() throws Exception {
        Assert.assertEquals(jsonResponseBody, "[{\"firstName\":\"John\"," +
                                                    "\"lastName\":\"Johnson\"," +
                                                    "\"creditCardNumber\":\"8472539283740184\"," +
                                                    "\"employeeId\":1," +
                                                    "\"mobile\":\"+447403764288\"," +
                                                    "\"credit\":100.0," +
                                                    "\"email\":\"johnjohnson@gmail.com\"," +
                                                    "\"cardRegistered\":true," +
                                                    "\"dataCard\":\"001ABC\"," +
                                                    "\"welcomeMessage\":null}]");
        Assert.assertEquals(new Long(1), employee.getEmployeeId());
    }

    @Test
    public void testCreditCardNumberHashing() {
        creditCardHasher = new CreditCardHasher();
        String plainCCNumber  = "plainCCNumber";
        String hashCCNumber = creditCardHasher.hashCreditCard(plainCCNumber);
        Assert.assertTrue(hashCCNumber.length() == 60);
        Assert.assertTrue(bCrypt.checkpw(plainCCNumber, hashCCNumber));
    }

    @Test
    public void testServiceLayerSetup() {
        Assert.assertEquals(employee.getFirstName(), "John");
        Assert.assertEquals(employee.getLastName(), "Johnson");
        Assert.assertEquals(employee.getCredit(), new Double(100.0));
        Assert.assertEquals(employee.getDataCard(), "001ABC");
        Assert.assertEquals(employee.getMobile(), "+447403764288");
        Assert.assertEquals(employee.getEmail(), "johnjohnson@gmail.com");
        Assert.assertEquals(employee.getCreditCardNumber().length(), 60);
        Assert.assertEquals(employee.getCardRegistered(), true);
    }

    @Test
    public void testCheckCredit() throws Exception {
        mockMvc.perform(get("/api/employees/credit/check/{id}", 2))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testTopUpCredit() throws Exception {
        mockMvc.perform(put("/api/employees/credit/topup/{id}", 1)
                .param("credit", String.valueOf(new Double(100.0))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testWithdrawCredit() throws Exception {
        mockMvc.perform(put("/api/employees/credit/withdraw/{id}", 2)
                .param("credit", String.valueOf(new Double(100.0))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testWithdrawCreditToNegativeValueNotPossible() throws Exception {
        mockMvc.perform(put("/api/employees/credit/withdraw/{id}", 2)
                .param("credit", "100000.0"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void testCheckCreditWithUnregisteredCard() throws Exception {
        mockMvc.perform(put("/api/employees/credit/withdraw/{id}", 1))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void testEndpointThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/employees/credit/hello"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testMessageWhenCardIsAlreadyRegistered() throws Exception {
        mockMvc.perform(get("/employees/scancard/{dataCard}", "001ABC"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome")))
                .andDo(print());
    }

    @Test
    public void testMessageWhenCardIsTappedASecondTime() throws Exception {
        mockMvc.perform(get("/employees/scancard/doubletap/{dataCard}", "001ABC"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Goodbye")))
                .andDo(print());
    }
}
