package com.harness.workshop.config;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import io.split.client.SplitManager;
import io.split.storages.enums.OperationMode;
import io.split.storages.enums.StorageMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pluggable.CustomStorageWrapper;
import redis.RedisInstance;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;


@Configuration
public class SplitConfig {

    @Value("${split.sdkKey}")
    private String sdkKey;

    @Value("${split.redisHost}")
    private String redisHost;

    @Value("${split.redisPort}")
    private int redisPort;

    @Value("${split.blockUntilReady:true}")
    private boolean blockUntilReady;

    @Value("${split.blockUntilReadyTimeoutMs:5000}")
    private int readyTimeoutMs;

    @Bean
    public SplitFactory splitFactory() throws Exception {
// Building the Redis storage wrapper with some configurations of choice.
//
       System.out.println(redisHost);
       System.out.println(redisPort);
//       CustomStorageWrapper redis = RedisInstance.builder()
//                .host(redisHost)
//                .port(redisPort)
//                .timeout(1000)
//                .database(0)
//                .build();
//
//
//        SplitClientConfig cfg = SplitClientConfig.builder()
//                .customStorageWrapper(redis)
//                .operationMode(OperationMode.CONSUMER)
//                .storageMode(StorageMode.REDIS)
//                .setBlockUntilReadyTimeout(10000)
//                .enableDebug()
//                .build();

        SplitClientConfig cfg = SplitClientConfig.builder()
                .setBlockUntilReadyTimeout(readyTimeoutMs)   // configures the timeout used by blockUntilReady()
                .build();
        return SplitFactoryBuilder.build(sdkKey, cfg);
    }

    @Bean
    public SplitClient splitClient(SplitFactory factory) throws Exception {
        SplitClient client = factory.client();
        if (blockUntilReady) {
            try {
                client.blockUntilReady();  // no args; uses the timeout from config
            } catch (TimeoutException | InterruptedException e) {
                // For a workshop/demo you can log and continue; in prod you might fail fast.
                // logger.warn("Split SDK not ready in time", e);
            }
        }
        return client;
    }

    @Bean
    public SplitManager splitManager(SplitFactory factory) {
        return factory.manager();
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public void setSdkKey(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public boolean isBlockUntilReady() {
        return blockUntilReady;
    }

    public void setBlockUntilReady(boolean blockUntilReady) {
        this.blockUntilReady = blockUntilReady;
    }

    public int getReadyTimeoutMs() {
        return readyTimeoutMs;
    }

    public void setReadyTimeoutMs(int readyTimeoutMs) {
        this.readyTimeoutMs = readyTimeoutMs;
    }
}



