package com.tvlicensing.tvlicensing.controller;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void register_validCustomer_redirects() throws Exception {
        doNothing().when(customerService).registerCustomer(any(Customer.class));

        mockMvc.perform(post("/register")
                        .param("fullName", "Jane Doe")
                        .param("email", "jane@example.com")
                        .param("password", "Password123!")
                        .param("confirmPassword", "Password123!")
                        .param("addressLine1", "1 Main Street")
                        .param("addressLine2", "")
                        .param("city", "Belfast")
                        .param("postcode", "BT11AA"))
                .andExpect(status().is3xxRedirection());

        verify(customerService).registerCustomer(any(Customer.class));
    }
    @Test
    void register_invalidEmail_showsFormErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("fullName", "Jane Doe")
                        .param("email", "bad-email")
                        .param("password", "Password123!")
                        .param("confirmPassword", "Password123!")
                        .param("addressLine1", "1 Main Street")
                        .param("addressLine2", "")
                        .param("city", "Belfast")
                        .param("postcode", "BT11AA"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("customer", "email"));
    }
    @Test
    void register_passwordMismatch_showsFormErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("fullName", "Jane Doe")
                        .param("email", "jane@example.com")
                        .param("password", "Password123!")
                        .param("confirmPassword", "Different123!")
                        .param("addressLine1", "1 Main Street")
                        .param("addressLine2", "")
                        .param("city", "Belfast")
                        .param("postcode", "BT11AA"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }
}
