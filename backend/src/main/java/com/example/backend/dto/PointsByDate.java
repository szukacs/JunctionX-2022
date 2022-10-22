package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PointsByDate {
    private Date date;
    private long count;
    private long pts;
    private long spending;
}
