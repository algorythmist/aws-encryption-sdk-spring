package com.tecacet.awssecurity.entity;

import com.tecacet.awssecurity.crypto.Encrypted;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @ToString.Include
    private String username;

    @Encrypted
    private String ssn;

    @Encrypted
    private String phoneNumber;

}
