package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.entity.Customer;
import com.tecacet.awssecurity.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class DirectCustomerService implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getByName(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("There is no customer with username " + username));
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
