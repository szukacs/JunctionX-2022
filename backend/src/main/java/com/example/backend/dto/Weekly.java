package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weekly {
    private int dayOfWeek;
    private long numberOfCheckouts;
}
