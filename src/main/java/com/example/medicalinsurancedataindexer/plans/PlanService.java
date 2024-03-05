package com.example.medicalinsurancedataindexer.plans;

import com.example.medicalinsurancedataindexer.util.RabbitMQHelper;
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
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;


public class PlanService {
    public String postPlan(String rawPlan) {
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

        String plan;
        try {
            String objectId = map.containsKey("objectId") ? map.get("objectId").toString() : null;
            if (objectId == null) {
                throw new PlanServiceException("Plan must have an objectId");
            }

            RedisHelper redisHelper = new RedisHelper();
            if (redisHelper.exist(objectId)) {
                throw new PlanAlreadyExistsException("Plan already exists");
            }

            RabbitMQHelper rabbitHelper = new RabbitMQHelper();
            rabbitHelper.publish(objectId+":"+rawPlan);

//            redisHelper.set(objectId, rawPlan);
//            plan = redisHelper.get(objectId);
            plan = rawPlan;
        } catch (Exception e) {
            if (e instanceof PlanNotFoundException) {
                throw new PlanNotFoundException(e.getMessage());
            }

            if (e instanceof PlanAlreadyExistsException) {
                throw new PlanAlreadyExistsException(e.getMessage());
            }

            throw new RedisException("Error setting plan in Redis: " + e.getMessage());
        }

        return plan;
    }

    public String getPlan(String id) {

        try {
            RedisHelper redisHelper = new RedisHelper();
            if (redisHelper.exist(id)) {
                return redisHelper.get(id);
            }

            throw new PlanNotFoundException("Plan not found");
        } catch (Exception e) {
            if (e instanceof PlanNotFoundException) {
                throw new PlanNotFoundException(e.getMessage());
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

            throw new PlanNotFoundException("Plan not found");
        } catch (Exception e) {
            if (e instanceof PlanNotFoundException) {
                throw new PlanNotFoundException(e.getMessage());
            }

            throw new RedisException("Error deleting plan from Redis: " + e.getMessage());
        }
    }

    public String putPlan(String rawPlan) {
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

        String plan;
        try {
            String objectId = map.containsKey("objectId") ? map.get("objectId").toString() : null;
            if (objectId == null) {
                throw new PlanServiceException("Plan must have an objectId");
            }

//            RedisHelper redisHelper = new RedisHelper();
//            redisHelper.set(objectId, rawPlan);
//            plan = redisHelper.get(objectId);
            RabbitMQHelper rabbitHelper = new RabbitMQHelper();
            rabbitHelper.publish(objectId+":"+rawPlan);

            plan = rawPlan;
        } catch (Exception e) {
            if (e instanceof PlanNotFoundException) {
                throw new PlanNotFoundException(e.getMessage());
            }

            if (e instanceof PlanAlreadyExistsException) {
                throw new PlanAlreadyExistsException(e.getMessage());
            }

            throw new RedisException("Error setting plan in Redis: " + e.getMessage());
        }

        return plan;
    }

    public String patchPlan(String id, String rawPlan) {
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

        try {
            RedisHelper redisHelper = new RedisHelper();
            String objectId = map.containsKey("objectId") ? map.get("objectId").toString() : null;
            if (objectId == null) {
                throw new PlanServiceException("Plan must have an objectId");
            }

            if (!redisHelper.exist(objectId)) {
                throw new PlanNotFoundException("Plan not found");
            }

            if (!redisHelper.exist(objectId)) {
//                redisHelper.set(objectId, rawPlan);
//                return redisHelper.get(objectId);
                RabbitMQHelper rabbitHelper = new RabbitMQHelper();
                rabbitHelper.publish(objectId+":"+rawPlan);
                return rawPlan;
            }

            String sOldPlan = redisHelper.get(objectId);
            Map<String, Object> oldMap = objectMapper.readValue(sOldPlan, new TypeReference<>() {});
            for (Entry<String, Object> entry : oldMap.entrySet()) {
                if (!map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), entry.getValue());
                    continue;
                }

                if (entry.getValue() instanceof ArrayList) {
                    List<?> newArray = (List<?>) map.get(entry.getKey());
                    List<?> oldArray = (List<?>) entry.getValue();
                    ArrayList<?> newArrayList = (ArrayList<?>)Stream.concat(newArray.stream(), oldArray.stream()).collect(Collectors.toList());
                    map.put(entry.getKey(), newArrayList);
                    continue;
                }

                if (entry.getValue() instanceof LinkedHashMap) {
                    LinkedHashMap<Object, Object> oldLinkedHashMap = (LinkedHashMap<Object, Object>) entry.getValue();
                    LinkedHashMap<Object, Object> newLinkedHashMap = (LinkedHashMap<Object, Object>) map.get(entry.getKey());
                    newLinkedHashMap.putAll(oldLinkedHashMap);
                    map.put(entry.getKey(), newLinkedHashMap);
                    continue;
                }
            }

            Object resPlan = objectMapper.convertValue(map, Object.class);
//            redisHelper.set(objectId, objectMapper.writeValueAsString(resPlan));
            RabbitMQHelper rabbitHelper = new RabbitMQHelper();
            String plan = objectMapper.writeValueAsString(resPlan);
            rabbitHelper.publish(objectId+":"+plan);

//            return redisHelper.get(objectId);
            return plan;
        } catch (Exception e) {
            if (e instanceof PlanNotFoundException) {
                throw new PlanNotFoundException(e.getMessage());
            }

            if (e instanceof PlanAlreadyExistsException) {
                throw new PlanAlreadyExistsException(e.getMessage());
            }

            throw new RedisException("Error setting plan in Redis: " + e.getMessage());
        }
    }
}
