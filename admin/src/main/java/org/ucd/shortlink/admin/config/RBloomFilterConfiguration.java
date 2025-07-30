package org.ucd.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBloomFilterConfiguration {

    /**
     * User Registration Query DB Bloom Filter
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(
                "userRegisterCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(10000000, 0.001);
        return cachePenetrationBloomFilter;
    }
}
