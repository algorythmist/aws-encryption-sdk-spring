package com.tecacet.awssecurity.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class AwsSdkEncryptionService implements EncryptionService {

    private final AwsCrypto crypto;

    private final CryptoMaterialsManager cryptoMaterialsManager;


    public AwsSdkEncryptionService(CryptoMaterialsManager cryptoMaterialsManager) {
        crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();
        this.cryptoMaterialsManager = cryptoMaterialsManager;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return crypto.encryptData(cryptoMaterialsManager, data).getResult();
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return crypto.decryptData(cryptoMaterialsManager, data).getResult();
    }

    @Override
    public String encrypt(String data) {
        byte[] encrypted = encrypt(data.getBytes(StandardCharsets.UTF_8));
        return Base64.toBase64String(encrypted);
    }

    @Override
    public String decrypt(String data) {
        byte[] decrypted = decrypt(Base64.decode(data));
        return new String(decrypted);
    }
}
