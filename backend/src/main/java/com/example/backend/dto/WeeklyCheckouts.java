package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyCheckouts {
    private LocalDate from;
    private LocalDate until;
    private int weeks;
    private List<DailyCheckout> dailyCheckouts;
}
