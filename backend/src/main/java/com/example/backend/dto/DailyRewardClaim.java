package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyRewardClaim {
    private Date date;
    private long numberOfRewardClaims;
    private long usedPoints;
}
