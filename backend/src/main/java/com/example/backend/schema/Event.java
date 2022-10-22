package com.example.backend.schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Document(collection = "events_junction")
public class Event {
    @Id private String id;
    private Date timestamp;
    private Date date;
    private long customer;
    private String action;
    private long points;
    private Date expdate;
    private boolean expired;
    private Map<String, Object> properties;
}
