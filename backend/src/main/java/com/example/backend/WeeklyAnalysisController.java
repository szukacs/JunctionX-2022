package com.example.backend;

import com.example.backend.dto.*;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
                project("id", "points").and(dayOfWeek).as("dayOfWeek").and("properties.total").as("price"),
                group("dayOfWeek").count().as("numberOfCheckouts")
                        .avg("points").as("avgPoints")
                        .avg("price").as("avgSpending"),
                project("numberOfCheckouts", "avgPoints", "avgSpending").and("dayOfWeek").previousOperation(),
                sort(Sort.Direction.ASC, "dayOfWeek")
        );
        var dailyCheckouts = mongoTemplate.aggregate(aggregation, Event.class, DailyCheckout.class);
        var response = new WeeklyCheckouts(from, until, weeks,
                dailyCheckouts.getMappedResults());
        return ResponseEntity.ok(response);
    }
}
