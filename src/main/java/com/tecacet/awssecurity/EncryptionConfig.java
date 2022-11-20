package com.tecacet.awssecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${aws.security.cache.capacity:100}")
    public int cacheCapacity;
    @Value("${aws.security.cache.maxMessages:100}")
    public int maxEntryMessages;
    @Value("${aws.security.cache.maxAge:1000}")
    public int maxEntryAge;

    @Value("${aws.key.arn}")
    public String keyArn;
}
