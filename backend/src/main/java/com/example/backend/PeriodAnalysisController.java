package com.example.backend;

import com.example.backend.dto.ActionCount;
import com.example.backend.dto.DailyActions;
import com.example.backend.dto.DailyExpire;
import com.example.backend.dto.PointsByDate;
import com.example.backend.schema.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs.absoluteValueOf;

@Tag(name = "Period Analysis API", description = "For getting data about events restricted to the given time period")
@RestController
@RequestMapping("/period")
@RequiredArgsConstructor
public class PeriodAnalysisController {
    private final MongoTemplate mongoTemplate;

    @Operation(summary = "Get the count of events and the sum of points for the given action on every day in the given time period")
    @GetMapping("/actions/{action}")
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

    @Operation(summary = "Get the number of points that will expire on any day in the given time period")
    @GetMapping("/expired-points")
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

    @Operation(summary = "Get the count of each event type(action) on every day in the given time period")
    @GetMapping("/actions")
    public ResponseEntity<?> getActionsByDay(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        var timeCriteria = Criteria.where("date").gte(from).lte(until);
        var aggregation = newAggregation(
                match(timeCriteria),
                project("id", "date", "action"),
                group("date", "action")
                        .count().as("count"),
                project("date", "action", "count"),
                sort(Sort.Direction.ASC, "date")
        );
        var dailyActions = mongoTemplate.aggregate(aggregation, Event.class, DailyActions.class).getMappedResults();
        Map<Date, Map<String, Long>> actionMap = new HashMap<>();
        dailyActions.forEach(dailyAction -> {
            var date = dailyAction.getDate();
            if (!actionMap.containsKey(date)) {
                actionMap.put(date, new HashMap<>());
            }
            var actionsOnDay = actionMap.get(date);
            actionsOnDay.put(dailyAction.getAction(), dailyAction.getCount());
        });
        return ResponseEntity.ok(actionMap);
    }
}
