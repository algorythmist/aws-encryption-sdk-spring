package com.tecacet.awssecurity.service;

import static org.junit.jupiter.api.Assertions.*;

import com.tecacet.awssecurity.entity.Customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void save() {
        var customer = new Customer();
        customer.setUsername("dauser");
        customer.setSsn("1234".getBytes());
        customerService.save(customer);

        var found = customerService.getByName("dauser");
        assertEquals("dauser", found.getUsername());
        assertEquals("1234", new String(found.getSsn()));
    }
}
