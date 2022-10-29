package com.tecacet.awssecurity.service;

import com.tecacet.awssecurity.domain.CustomerDto;
import com.tecacet.awssecurity.entity.Customer;

public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .ssn(new String(customer.getSsn()))
                .phoneNumber(new String(customer.getPhoneNumber()))
                .build();
    }

    public Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUsername(dto.getUsername());
        customer.setPhoneNumber(dto.getPhoneNumber().getBytes());
        customer.setSsn(dto.getSsn().getBytes());
        return customer;
    }
}
