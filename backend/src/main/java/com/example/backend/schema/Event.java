package com.example.backend.schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "events_junction")
public class Event {
    @Id private String id;
    private long points;
    private Date timestamp;
    private long customer;
    private String action;
}
