package com.tecacet.awssecurity.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.caching.CachingCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache;
import com.amazonaws.encryptionsdk.caching.LocalCryptoMaterialsCache;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.tecacet.awssecurity.EncryptionCacheConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//TODO: use encryption context: https://docs.aws.amazon.com/encryption-sdk/latest/developer-guide/best-practices.html
@Service
public class EncryptionService {

    private final AwsCrypto crypto;
    private final KmsMasterKeyProvider keyProvider;

    private final CryptoMaterialsManager cachingManager;

    public EncryptionService(@Value("${aws.key.arn}") String keyArn, EncryptionCacheConfig cacheConfig) {
        crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();
        keyProvider = KmsMasterKeyProvider.builder()
                .buildStrict(keyArn);

        CryptoMaterialsCache cache = new LocalCryptoMaterialsCache(cacheConfig.cacheCapacity);
        cachingManager =
                CachingCryptoMaterialsManager.newBuilder().withMasterKeyProvider(keyProvider)
                        .withCache(cache)
                        .withMaxAge(cacheConfig.maxEntryAge, TimeUnit.SECONDS)
                        .withMessageUseLimit(cacheConfig.maxEntryMessages)
                        .build();
    }

    public CryptoResult<byte[], ?> encrypt(byte[] data) {
        return crypto.encryptData(cachingManager, data);
    }

    public CryptoResult<byte[], ?> decrypt(byte[] data) {
        return crypto.decryptData(cachingManager, data);
    }
}
