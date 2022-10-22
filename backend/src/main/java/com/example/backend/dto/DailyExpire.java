package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyExpire {
    private Date expdate;
    private long numberOfExpires;
    private long expiredPoints;
}
