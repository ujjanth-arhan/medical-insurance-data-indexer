package com.example.medicalinsurancedataindexer.plans;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/")
public class PlanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);
    private final PlanService planService;

    PlanController() {
        this.planService = new PlanService();
    }

    @GetMapping(path = "/plan/{id}")
    ResponseEntity<?> get(HttpServletResponse response, @PathVariable(value = "id") String id) {
        LOGGER.trace("Getting plan with id: " + id);

        String plan = planService.getPlan(id);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(plan);

    }

    @GetMapping(path = "/plan")
    ResponseEntity<?> getAll() {
        LOGGER.trace("Get all plans");

        List<Object> plans = planService.getAllPlans();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(plans);
    }

    @PostMapping(path = "/plan")
    ResponseEntity<?> post(HttpServletResponse response, @RequestBody String rawPlan) {
        LOGGER.trace("Creating plan: " + rawPlan);

        planService.setPlan(rawPlan);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new Object() {
                    public final String message = "Plan created";
                });
    }

    @DeleteMapping(path = "/plan/{id}")
    ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        LOGGER.trace("Deleting plan with id: " + id);

        planService.deletePlan(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new Object() {
                    public final String message = "Plan deleted";
                });
    }

}
