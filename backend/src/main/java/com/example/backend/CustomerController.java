package com.example.backend;

import com.example.backend.schema.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer API", description = "Utility for getting basic event data of customers")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final MongoTemplate mongoTemplate;

    @Operation(summary = "Get all the events of a customer that has the given customer id")
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerEvents(@PathVariable long customerId) {
        var query = new Query();
        query.addCriteria(Criteria.where("customer").is(customerId));
        return ResponseEntity.ok(mongoTemplate.find(query, Event.class));
    }

}
