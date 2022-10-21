package com.example.backend;

import com.example.backend.dto.Weekly;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InsightController {

    private final MongoTemplate mongoTemplate;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerEvents(@PathVariable long customerId) {
        var query = new Query();
        query.addCriteria(Criteria.where("customer").is(customerId));
        return ResponseEntity.ok(mongoTemplate.find(query, Event.class));
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyCheckouts(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
    ) {
        var timeCriteria = from != null && to != null ?
                Criteria.where("timestamp").gte(from).lte(to).nin()
                : from != null ? Criteria.where("timestamp").gte(from)
                : to != null ? Criteria.where("timestamp").lte(to) : new Criteria();
        var dayOfWeek = DateOperators.DayOfWeek.dayOfWeek("timestamp");
        var aggregation = newAggregation(
                match(timeCriteria),
                match(Criteria.where("action").is("checkout")),
                project("id").and(dayOfWeek).as("dayOfWeek"),
                group("dayOfWeek").count().as("numberOfCheckouts"),
                project("numberOfCheckouts").and("dayOfWeek").previousOperation(),
                sort(Sort.Direction.ASC, "dayOfWeek")
        );
        return ResponseEntity.ok(mongoTemplate.aggregate(aggregation, Event.class, Weekly.class).getMappedResults());
    }
}
