package com.tecacet.awssecurity.service;

import static org.junit.jupiter.api.Assertions.*;

import com.tecacet.awssecurity.crypto.HibernateDecryptListener;
import com.tecacet.awssecurity.crypto.HibernateEncryptListener;
import com.tecacet.awssecurity.entity.Customer;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @SpyBean
    private HibernateEncryptListener encryptListener;

    @SpyBean
    private HibernateDecryptListener decryptListener;

    @Test
    void save() {
        var customer = new Customer();
        customer.setUsername("dauser");
        customer.setSsn("1234".getBytes());
        customerService.save(customer);

        var found = customerService.getByName("dauser");
        assertEquals("dauser", found.getUsername());
        assertEquals("1234", new String(found.getSsn()));

        Mockito.verify(encryptListener).onPreInsert(Mockito.any(PreInsertEvent.class));
        Mockito.verify(decryptListener).onPostLoad(Mockito.any(PostLoadEvent.class));
    }
}
