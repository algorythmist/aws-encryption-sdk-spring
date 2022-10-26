package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.entity.Customer;

public interface CustomerService {
    Customer getByName(String username);

    void save(Customer customer);
}
