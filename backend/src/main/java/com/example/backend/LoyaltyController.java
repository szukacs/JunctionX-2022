package com.example.backend;

import com.example.backend.dto.CustomerLoyalty;
import com.example.backend.dto.CustomerRewards;
import com.example.backend.schema.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Tag(name = "Loyalty API", description = "For finding correlation between customer spending, reward claims and time spent in the loyalty program")
@RestController
@RequestMapping("/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {
    private final MongoTemplate mongoTemplate;

    @Operation(summary = "Get the number of active days, opt-in date, last active day and total spending for each customer")
    @GetMapping
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
                limit(5000),
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
