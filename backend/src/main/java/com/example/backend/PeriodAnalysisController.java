package com.example.backend;

import com.example.backend.dto.PointsByDate;
import com.example.backend.schema.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
