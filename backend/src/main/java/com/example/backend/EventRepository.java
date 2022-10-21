package com.example.backend;

import com.example.backend.schema.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "events_junction", path = "events_junction")
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findAll();
}
