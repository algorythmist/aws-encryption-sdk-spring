package com.tecacet.awssecurity.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CachingCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache;
import com.amazonaws.encryptionsdk.caching.LocalCryptoMaterialsCache;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.tecacet.awssecurity.EncryptionConfig;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class AwsSdkEncryptionService implements EncryptionService {

    private final AwsCrypto crypto;

    private final CryptoMaterialsManager cachingManager;

    public AwsSdkEncryptionService(EncryptionConfig config) {
        crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();
        var keyProvider = KmsMasterKeyProvider.builder().buildStrict(config.keyArn);

        CryptoMaterialsCache cache = new LocalCryptoMaterialsCache(config.cacheCapacity);
        cachingManager =
                CachingCryptoMaterialsManager.newBuilder().withMasterKeyProvider(keyProvider)
                        .withCache(cache)
                        .withMaxAge(config.maxEntryAge, TimeUnit.SECONDS)
                        .withMessageUseLimit(config.maxEntryMessages)
                        .build();
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return crypto.encryptData(cachingManager, data).getResult();
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return crypto.decryptData(cachingManager, data).getResult();
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
