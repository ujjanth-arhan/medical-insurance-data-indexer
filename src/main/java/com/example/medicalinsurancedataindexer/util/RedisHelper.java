package com.example.medicalinsurancedataindexer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RedisHelper {

    private final Jedis jedis;

    public RedisHelper() {
        this.jedis = new Jedis();
    }

    public void set(String key, String value) {
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            throw new RedisException("Error setting key: " + key + " value: " + value + " in Redis." + e.getMessage());
        }
    }

    public String get(String key) {
        try {
            return jedis.get(key);
        } catch (Exception e) {
            throw new RedisException("Error getting key: " + key + " from Redis." + e.getMessage());
        }
    }

    public List<Object> getAll() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Set<String> keys = jedis.keys("*");
            List<String> rawPlans = jedis.mget(keys.toArray(new String[0]));

            return rawPlans.stream().map(rawPlan -> {
                try {
                    return objectMapper.readValue(rawPlan, Object.class);
                } catch (Exception e) {
                    throw new RedisException("Error mapping plan: " + e.getMessage());
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RedisException("Error getting all keys from Redis." + e.getMessage());
        }
    }

    public boolean exist(String key) {
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            throw new RedisException("Error checking for key: " + key + " from Redis." + e.getMessage());
        }
    }

    public void delete(String key) {
        try {
            jedis.del(key);
        } catch (Exception e) {
            throw new RedisException("Error deleting key: " + key + " from Redis." + e.getMessage());
        }
    }
}
