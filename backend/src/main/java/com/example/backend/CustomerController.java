package com.example.backend;

import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final MongoTemplate mongoTemplate;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerEvents(@PathVariable long customerId) {
        var query = new Query();
        query.addCriteria(Criteria.where("customer").is(customerId));
        return ResponseEntity.ok(mongoTemplate.find(query, Event.class));
    }

}
