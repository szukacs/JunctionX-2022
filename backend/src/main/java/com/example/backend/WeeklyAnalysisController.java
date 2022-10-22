package com.example.backend;

import com.example.backend.dto.*;
import com.example.backend.schema.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Tag(name = "Weekly Analysis API", description = "For getting event data projected to the days of the week")
@RestController
@RequestMapping("/week")
@RequiredArgsConstructor
public class WeeklyAnalysisController {

    private final MongoTemplate mongoTemplate;

    @Operation(summary = "Get the average weekly point gain and spending")
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
                dailyCheckouts.getMappedResults().stream().map(checkout -> {
                    checkout.setNumberOfCheckouts(checkout.getNumberOfCheckouts() / weeks);
                    return checkout;
                }).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
