package com.example.backend;

import com.example.backend.dto.*;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs.absoluteValueOf;

@RestController
@RequestMapping("/week")
@RequiredArgsConstructor
public class WeeklyAnalysisController {

    private final MongoTemplate mongoTemplate;

    @GetMapping("/checkouts")
    public ResponseEntity<?> getWeeklyCheckouts(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false, defaultValue = "1") int weeks
    ) {
        var until = from != null ? from.plusWeeks(weeks) : null;
        var timeCriteria = from != null ?
                Criteria.where("timestamp").gte(from).lte(until).nin()
                : new Criteria();
        var dayOfWeek = DateOperators.DayOfWeek.dayOfWeek("timestamp");
        var aggregation = newAggregation(
                match(timeCriteria),
                match(Criteria.where("action").is("checkout")),
                project("id").and(dayOfWeek).as("dayOfWeek"),
                group("dayOfWeek").count().as("numberOfCheckouts"),
                project("numberOfCheckouts").and("dayOfWeek").previousOperation(),
                sort(Sort.Direction.ASC, "dayOfWeek")
        );
        var dailyCheckouts = mongoTemplate.aggregate(aggregation, Event.class, DailyCheckout.class);
        var response = new WeeklyCheckouts(from, until, weeks, dailyCheckouts.getMappedResults());
        return ResponseEntity.ok(response);
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
                project("id", "expdate", "points"),
                group("expdate")
                        .count().as("numberOfExpires")
                        .sum(absoluteValueOf("points")).as("expiredPoints"),
                project("numberOfExpires", "expiredPoints").and("expdate").previousOperation(),

                sort(Sort.Direction.ASC, "expdate")
        );
        var dailyExpires = mongoTemplate.aggregate(aggregation, Event.class, DailyExpire.class);
        return ResponseEntity.ok(dailyExpires.getMappedResults());
    }

    @GetMapping("/grantedPoints")
    public ResponseEntity<?> getGrantedPoints(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        var timeCriteria = Criteria.where("date").gte(from).lte(until);
        var aggregation = newAggregation(
                match(timeCriteria),
                //match(Criteria.where("action").is("points_expired")),
                match(Criteria.where("expdate").exists(true).and("points").gt(0)),
                project("id", "date", "points"),
                group("date")
                        .count().as("numberOfPointGrants")
                        .sum(absoluteValueOf("points")).as("grantedPoints"),
                project("numberOfPointGrants", "grantedPoints").and("date").previousOperation(),

                sort(Sort.Direction.ASC, "date")
        );
        var dailyExpires = mongoTemplate.aggregate(aggregation, Event.class, DailyGrant.class);
        return ResponseEntity.ok(dailyExpires.getMappedResults());
    }

    @GetMapping("/claimedRewards")
    public ResponseEntity<?> getClaimedRewards(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        var timeCriteria = Criteria.where("date").gte(from).lte(until);
        var aggregation = newAggregation(
                match(timeCriteria),
                match(Criteria.where("action").is("reward")),
                project("id", "date", "points"),
                group("date")
                        .count().as("numberOfRewardClaims")
                        .sum(absoluteValueOf("points")).as("usedPoints"),
                project("numberOfRewardClaims", "usedPoints").and("date").previousOperation(),

                sort(Sort.Direction.ASC, "date")
        );
        var dailyExpires = mongoTemplate.aggregate(aggregation, Event.class, DailyRewardClaim.class);
        return ResponseEntity.ok(dailyExpires.getMappedResults());
    }
}
