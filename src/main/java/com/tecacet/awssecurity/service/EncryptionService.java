package com.tecacet.awssecurity.service;

public interface EncryptionService {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);
}
