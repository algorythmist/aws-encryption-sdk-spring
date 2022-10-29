package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.domain.CustomerDto;
import com.tecacet.awssecurity.entity.Customer;
import com.tecacet.awssecurity.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class DirectCustomerService implements CustomerService {

    private final CustomerMapper customerMapper = new CustomerMapper();
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto getByName(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("There is no customer with username " + username));
        return customerMapper.toDto(customer);
    }

    @Override
    public void save(CustomerDto customer) {
        customerRepository.save(customerMapper.toEntity(customer));
    }

}
