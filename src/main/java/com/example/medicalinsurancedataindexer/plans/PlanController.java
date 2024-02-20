package com.example.medicalinsurancedataindexer.plans;

import com.example.medicalinsurancedataindexer.util.ETagHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
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

    @GetMapping(path = "/plan/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> get(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id") String id) {
        LOGGER.trace("Getting plan with id: " + id);

        String plan = planService.getPlan(id);
        if (request.getHeader("If-None-Match") != null && request.getHeader("If-None-Match").equals(ETagHelper.generateETag(plan))) {
            return ResponseEntity
                    .status(HttpStatus.NOT_MODIFIED)
                    .eTag(ETagHelper.generateETag(plan))
                    .body(new Object() {
                        public final String message = "Not modified";
                    });
        }

        return  ResponseEntity
                .status(HttpStatus.OK)
                .eTag(ETagHelper.generateETag(plan))
                .body(plan);

    }

    @GetMapping(path = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAll() {
        LOGGER.trace("Get all plans");

        List<Object> plans = planService.getAllPlans();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(plans);
    }

    @PostMapping(path = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> post(HttpServletResponse response, @RequestBody String rawPlan) {
        LOGGER.trace("Creating plan: " + rawPlan);

        String plan = planService.setPlan(rawPlan);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag(ETagHelper.generateETag(plan))
                .body(plan);
    }

    @DeleteMapping(path = "/plan/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        LOGGER.trace("Deleting plan with id: " + id);

        planService.deletePlan(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new Object() {
                    public final String message = "Plan deleted";
                });
    }

}
