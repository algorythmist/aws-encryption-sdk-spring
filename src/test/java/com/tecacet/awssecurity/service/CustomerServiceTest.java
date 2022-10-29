package com.tecacet.awssecurity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tecacet.awssecurity.crypto.HibernateDecryptListener;
import com.tecacet.awssecurity.crypto.HibernateEncryptListener;
import com.tecacet.awssecurity.domain.CustomerDto;
import com.tecacet.awssecurity.repository.CustomerRepository;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class CustomerServiceTest {

    private final CustomerMapper customerMapper = new CustomerMapper();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @SpyBean
    private HibernateEncryptListener encryptListener;

    @SpyBean
    private HibernateDecryptListener decryptListener;

    @Test
    void saveEncryptDecrypt() {
        var customer = createCustomer("dauser", "1234", "999");
        customerService.save(customer);

        var found = customerService.getByName("dauser");
        assertEquals("dauser", found.getUsername());
        assertEquals("1234", found.getSsn());
        assertEquals("999", found.getPhoneNumber());

        Mockito.verify(encryptListener).onPreInsert(Mockito.any(PreInsertEvent.class));
        Mockito.verify(decryptListener).onPostLoad(Mockito.any(PostLoadEvent.class));
    }

    @Test
    void multipleCustomers() {
        var customers = IntStream.range(0, 100)
                .mapToObj(i -> createCustomer("user" + i, "ssn" + i, "phone" + i))
                .map(customerMapper::toEntity)
                .collect(Collectors.toList());
        customerRepository.saveAll(customers);

        var found = customerRepository.findAll();
        assertEquals(100, found.size());
        IntStream.range(0, 100).forEach(i -> {
            assertEquals("ssn" + i, new String(found.get(i).getSsn()));
            assertEquals("phone" + i, new String(found.get(i).getPhoneNumber()));
        });
    }


    private CustomerDto createCustomer(String username, String ssn, String phoneNumber) {
        return CustomerDto.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .ssn(ssn)
                .build();
    }

    @BeforeEach
    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll();
    }

}
