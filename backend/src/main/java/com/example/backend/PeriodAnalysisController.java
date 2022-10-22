package com.example.backend;

import com.example.backend.dto.CustomerLoyalty;
import com.example.backend.dto.DailyExpire;
import com.example.backend.dto.PointsByDate;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs.absoluteValueOf;

@RestController
@RequestMapping("/period")
@RequiredArgsConstructor
public class PeriodAnalysisController {
    private final MongoTemplate mongoTemplate;

    @GetMapping("/{action}")
    public ResponseEntity<?> getPointsByDateForAction(
            @PathVariable String action,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") LocalDateTime from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") LocalDateTime until
    ) {
        return ResponseEntity.ok(getPointsByDateAndAction(action, from, until));
    }

    private List<PointsByDate> getPointsByDateAndAction(String action, LocalDateTime from, LocalDateTime until) {
        var timeCriteria = Criteria.where("timestamp").gte(from).lte(until);
        var aggregation = newAggregation(
                match(timeCriteria),
                match(Criteria.where("action").is(action)),
                project("id", "date", "points").and("properties.total").as("prc"),
                group("date")
                        .count().as("count")
                        .sum(absoluteValueOf("points")).as("pts")
                        .sum(absoluteValueOf("prc")).as("spending"),
                project("count", "pts", "spending").and("date").previousOperation(),
                sort(Sort.Direction.ASC, "date")
        );
        return mongoTemplate.aggregate(aggregation, Event.class, PointsByDate.class).getMappedResults();
    }

    @GetMapping("/expiredPoints")
    public ResponseEntity<?> getExpiredPoints(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        var timeCriteria = Criteria.where("expdate").gte(from).lte(until);
        var aggregation = newAggregation(
                match(timeCriteria),
                //match(Criteria.where("action").is("points_expired")),
                match(Criteria.where("expdate").exists(true).and("points").gt(0)),
                project("id", "points").and("expdate").as("date"),
                group("date")
                        .count().as("numberOfExpires")
                        .sum(absoluteValueOf("points")).as("expiredPoints"),
                project("numberOfExpires", "expiredPoints").and("date").previousOperation(),

                sort(Sort.Direction.ASC, "date")
        );
        var dailyExpires = mongoTemplate.aggregate(aggregation, Event.class, DailyExpire.class);
        return ResponseEntity.ok(dailyExpires.getMappedResults());
    }

    @GetMapping("/loyalty")
    public ResponseEntity<?> getLoyaltyInDays() {
        var aggregation = newAggregation(
                //match(Criteria.where("customer").is(391705)),
                project("id", "customer", "date", "action")
                        .and("properties.subtotal").as("money"),
                group("customer")
                        .min("date").as("optInDate")
                        .max("date").as("lastEventDate")
                        .sum("money").as("spending"),
                limit(1000),
                project("optInDate", "lastEventDate", "spending")
                        .and("lastEventDate").minus("optInDate").as("loyalDays")
                        .and("customer").previousOperation()
        ).withOptions(AggregationOptions.builder().allowDiskUse(true).build());
        var result = mongoTemplate.aggregate(aggregation, Event.class, CustomerLoyalty.class);
        //var result = mongoTemplate.aggregate(aggregation, Event.class, Map.class);
        return ResponseEntity.ok(result.getMappedResults()
                .stream().map(cl -> {
                    cl.setLoyalDays(cl.getLoyalDays() / 86_400_000);
                    return cl;
                }).collect(Collectors.toList()));
    }
}
