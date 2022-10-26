package com.tecacet.awssecurity.entity;

import com.tecacet.awssecurity.crypto.Encrypted;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ToString.Include
    private String username;

    @Column(length = 1000)
    @Encrypted
    private byte[] ssn;

    @Column(length = 1000)
    @Encrypted
    private byte[] phoneNumber;

}
