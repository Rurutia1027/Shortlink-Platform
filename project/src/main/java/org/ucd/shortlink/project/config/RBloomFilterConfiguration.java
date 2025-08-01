package org.ucd.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBloomFilterConfiguration {

    /**
     * Redisson based Bloom Filter avoid high frequent querying DB for generating unique
     * Short Link URI.
     */
    @Bean
    public RBloomFilter<String> shortUriCreationCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(
                "shortUriCreationCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(10000000, 0.001);
        return cachePenetrationBloomFilter;
    }
}
