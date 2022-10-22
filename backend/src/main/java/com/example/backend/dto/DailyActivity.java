package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyActivity {
    private Date date;
    private long numberOfActivities;
    private long rewardedPoints;
}
