package com.tecacet.awssecurity.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CustomerDto {

    private UUID id;
    private String username;
    private String ssn;
    private String phoneNumber;
}
