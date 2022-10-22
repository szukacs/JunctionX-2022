package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyCheckouts {
    private int dayOfWeek;
    private long numberOfCheckouts;
}
