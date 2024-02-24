package com.example.medicalinsurancedataindexer.plans;

import com.example.medicalinsurancedataindexer.security.OAuthGoogle;
import com.example.medicalinsurancedataindexer.util.ETagHelper;
import jakarta.servlet.http.HttpServletRequest;
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

    @OAuthGoogle
    @GetMapping(path = "/plan/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> get(HttpServletRequest request, @PathVariable(value = "id") String id) {
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

    @OAuthGoogle
    @GetMapping(path = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAll() {
        LOGGER.trace("Get all plans");

        List<Object> plans = planService.getAllPlans();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(plans);
    }

    @OAuthGoogle
    @PostMapping(path = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> post(HttpServletRequest request, @RequestBody String rawPlan) {
        LOGGER.trace("Creating plan: " + rawPlan);

        String plan = planService.postPlan(rawPlan);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag(ETagHelper.generateETag(plan))
                .body(plan);
    }

    @OAuthGoogle
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

    /**
     * Create or replace a plan does not take id as a parameter, it is extracted from the plan itself.
     * The plan is expected to have an objectId field which would be used for this purpose.
     * @param rawPlan
     * @return
     */
    @OAuthGoogle
    @PutMapping(path = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> put(@RequestBody String rawPlan) {
        LOGGER.trace("Creating/Replacing plan");

        String plan = planService.putPlan(rawPlan);

        return ResponseEntity
                .status(HttpStatus.OK)
                .eTag(ETagHelper.generateETag(plan))
                .body(plan);
    }

    @OAuthGoogle
    @PatchMapping(path = "/plan/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> patch(HttpServletRequest request, @PathVariable(value = "id") String id, @RequestBody String rawPlan) {
        LOGGER.trace("Patching plan with id: " + id);

        String plan = planService.patchPlan(id, rawPlan);

        return ResponseEntity
                .status(HttpStatus.OK)
                .eTag(ETagHelper.generateETag(plan))
                .body(plan);
    }

}
