package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyGrant {
    private Date date;
    private long numberOfPointGrants;
    private long grantedPoints;
}
