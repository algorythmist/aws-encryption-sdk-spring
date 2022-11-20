package com.tecacet.awssecurity;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CachingCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache;
import com.amazonaws.encryptionsdk.caching.LocalCryptoMaterialsCache;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class EncryptionConfig {

    private static final String AWS_LOCALSTACK_ENDPOINT = "http://127.0.0.1:4566";
    private static final String AWS_REGION = "us-east-1";
    @Value("${aws.security.cache.capacity:100}")
    private int cacheCapacity;
    @Value("${aws.security.cache.maxMessages:100}")
    private int maxEntryMessages;
    @Value("${aws.security.cache.maxAge:1000}")
    private int maxEntryAge;
    @Value("${aws.key.arn}")
    private String keyArn;

    @Value("${aws.localstack:false}")
    private boolean useLocalstack;

    @Bean
    KmsMasterKeyProvider keyProvider() {
        return useLocalstack ?
                KmsMasterKeyProvider.builder()
                        .withCustomClientFactory(new LocalstackSupplier())
                        .buildStrict(keyArn)
                : KmsMasterKeyProvider.builder().buildStrict(keyArn);
    }

    @Bean
    CryptoMaterialsManager cryptoMaterialsManager(KmsMasterKeyProvider keyProvider) {
        CryptoMaterialsCache cache = new LocalCryptoMaterialsCache(cacheCapacity);
        return
                CachingCryptoMaterialsManager.newBuilder().withMasterKeyProvider(keyProvider)
                        .withCache(cache)
                        .withMaxAge(maxEntryAge, TimeUnit.SECONDS)
                        .withMessageUseLimit(maxEntryMessages)
                        .build();
    }

    private class LocalstackSupplier implements KmsMasterKeyProvider.RegionalClientSupplier {

        @Override
        public AWSKMS getClient(String s) {
            return AWSKMSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("", "")))
                    .withEndpointConfiguration(new AwsClientBuilder
                            .EndpointConfiguration(AWS_LOCALSTACK_ENDPOINT, AWS_REGION)).build();
        }
    }
}
