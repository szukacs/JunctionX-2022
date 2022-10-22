package com.example.backend;

import com.example.backend.dto.CustomerLoyalty;
import com.example.backend.dto.CustomerRewards;
import com.example.backend.dto.DailyExpire;
import com.example.backend.dto.PointsByDate;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        var options = AggregationOptions.builder().allowDiskUse(true).build();
        var aggregation = newAggregation(
                //match(Criteria.where("customer").is(391705)),
                project("id", "date", "action")
                        .andExpression("toString(customer)").as("customer")
                        .and("properties.subtotal").as("money"),
                group("customer")
                        .min("date").as("optInDate")
                        .max("date").as("lastEventDate")
                        .sum("money").as("spending")
                ,
                limit(1000),
                project("optInDate", "lastEventDate", "spending")
                        .and("lastEventDate").minus("optInDate").as("loyalDays")
                        .and("customer").previousOperation()
        ).withOptions(options);
        var result = mongoTemplate.aggregate(aggregation, Event.class, CustomerLoyalty.class).getMappedResults();
        //var result = mongoTemplate.aggregate(aggregation, Event.class, Map.class);
        var customerIds = result.stream().map(CustomerLoyalty::getCustomer).toList();
        var customerRewards = mongoTemplate.aggregate(
                newAggregation(
                        project("id", "action").andExpression("toString(customer)").as("customer"),
                        match(Criteria.where("customer").in(customerIds).and("action").is("reward")),
                        group("customer").count().as("rewards"),
                        project("rewards").and("customer").previousOperation()
                ).withOptions(options),
                Event.class,
                CustomerRewards.class
        ).getMappedResults();
        return ResponseEntity.ok(result
                .stream().map(cl -> {
                    cl.setLoyalDays(cl.getLoyalDays() / 86_400_000);
                    cl.setRewardsClaimed(customerRewards.stream()
                            .filter(cr -> Objects.equals(cr.getCustomer(), cl.getCustomer()))
                            .findFirst().map(CustomerRewards::getRewards).stream().findFirst().orElse(0L));
                    return cl;
                }).collect(Collectors.toList()));
    }
}
