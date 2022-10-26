package com.tecacet.awssecurity.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//TODO: use encryption context: https://docs.aws.amazon.com/encryption-sdk/latest/developer-guide/best-practices.html
@Service
public class EncryptionService {

    private final AwsCrypto crypto;
    private final KmsMasterKeyProvider provider;

    public EncryptionService(@Value("${aws.key.arn}") String keyArn) {
        crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();
        provider = KmsMasterKeyProvider.builder()
                .buildStrict(keyArn);
    }

    public CryptoResult<byte[], KmsMasterKey> encrypt(byte[] data) {
        return crypto.encryptData(provider, data);
    }

    public CryptoResult<byte[], KmsMasterKey> decrypt(byte[] data) {
        return crypto.decryptData(provider, data);
    }
}
