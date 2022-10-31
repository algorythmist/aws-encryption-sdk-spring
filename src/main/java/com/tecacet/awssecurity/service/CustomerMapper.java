package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.domain.CustomerDto;
import com.tecacet.awssecurity.entity.Customer;

public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .ssn(customer.getSsn())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }

    public Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUsername(dto.getUsername());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setSsn(dto.getSsn());
        return customer;
    }
}
