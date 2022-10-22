package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyActions {
    private Date date;
    private String action;
    private long count;
}
