package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.domain.CustomerDto;

public interface CustomerService {
    CustomerDto getByName(String username);

    void save(CustomerDto customer);
}
