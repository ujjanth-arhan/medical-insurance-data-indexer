package com.example.medicalinsurancedataindexer.plans;

import com.example.medicalinsurancedataindexer.util.RedisException;
import com.example.medicalinsurancedataindexer.util.RedisHelper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.everit.json.schema.Schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class PlanService {
    public void setPlan(String rawPlan) {
        try (InputStream inputStream = getClass().getResourceAsStream("/PlanSchema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(rawPlan));
        } catch (IOException | NullPointerException e) {
            throw new PlanServiceException("Error loading schema: " + e.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(rawPlan, new TypeReference<>() {});
        } catch (JsonMappingException e) {
            throw new PlanServiceException("Error mapping plan: " + e.getMessage());
        } catch (IOException e) {
            throw new PlanServiceException("Error reading plan: " + e.getMessage());
        }

        String objectId = map.containsKey("objectId") ? map.get("objectId").toString() : null;
        if (objectId == null) {
            throw new PlanServiceException("Plan must have an objectId");
        }

        try {
            RedisHelper redisHelper = new RedisHelper();
            redisHelper.set(objectId, rawPlan);
        } catch (Exception e) {
            throw new RedisException("Error setting plan in Redis: " + e.getMessage());
        }
    }

    public String getPlan(String id) {

        try {
            RedisHelper redisHelper = new RedisHelper();
            if (redisHelper.exist(id)) {
                return redisHelper.get(id);
            }

            throw new PlanAvailabilityException("Plan not found");
        } catch (Exception e) {
            if (e instanceof PlanAvailabilityException) {
                throw new PlanAvailabilityException(e.getMessage());
            }

            throw new RedisException("Error getting plan from Redis: " + e.getMessage());
        }
    }

    public List<Object> getAllPlans() {
        try {
            RedisHelper redisHelper = new RedisHelper();
            return redisHelper.getAll();
        } catch (Exception e) {
            throw new RedisException("Error getting plans from Redis: " + e.getMessage());
        }
    }

    public void deletePlan(String id) {
        try {
            RedisHelper redisHelper = new RedisHelper();
            if (redisHelper.exist(id)) {
                redisHelper.delete(id);
                return;
            }

            throw new PlanAvailabilityException("Plan not found");
        } catch (Exception e) {
            if (e instanceof PlanAvailabilityException) {
                throw new PlanAvailabilityException(e.getMessage());
            }

            throw new RedisException("Error deleting plan from Redis: " + e.getMessage());
        }
    }
}
